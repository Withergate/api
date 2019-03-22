package com.withergate.api.service.location;

import com.withergate.api.GameProperties;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.Clan;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.encounter.ICombatService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.INotificationService;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Location service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class LocationService implements ILocationService {

    private final LocationActionRepository locationActionRepository;
    private final GameProperties gameProperties;
    private final ClanService clanService;
    private final CharacterService characterService;
    private final RandomService randomService;
    private final EncounterService encounterService;
    private final ItemService itemService;
    private final ICombatService combatService;
    private final INotificationService notificationService;

    public LocationService(LocationActionRepository locationActionRepository,
                           GameProperties gameProperties, ClanService clanService,
                           CharacterService characterService,
                           RandomService randomService,
                           EncounterService encounterService, ItemService itemService,
                           ICombatService combatService,
                           INotificationService notificationService) {
        this.locationActionRepository = locationActionRepository;
        this.gameProperties = gameProperties;
        this.clanService = clanService;
        this.characterService = characterService;
        this.randomService = randomService;
        this.encounterService = encounterService;
        this.itemService = itemService;
        this.combatService = combatService;
        this.notificationService = notificationService;
    }

    @Override
    public void saveLocationAction(LocationAction action) {
        locationActionRepository.save(action);
    }

    @Transactional
    @Override
    public void processLocationActions(int turnId) {
        log.info("Executing location actions...");
        List<LocationAction> actions = locationActionRepository.findAllByState(ActionState.PENDING);

        for (LocationAction action : actions) {
            Character character = action.getCharacter();

            log.debug("Character {} is exploring {}.", character.getName(), action.getLocation());

            // prepare notification
            ClanNotification notification = new ClanNotification();
            notification.setClanId(character.getClan().getId());
            notification.setTurnId(turnId);

            switch (action.getLocation()) {
                case NEIGHBORHOOD:
                    processLocationAction(notification, character, action.getLocation(),
                            gameProperties.getNeighborhoodEncounterProbability(),
                            gameProperties.getNeighborhoodLootProbability(),
                            gameProperties.getNeighborhoodJunkMultiplier(), gameProperties.getNeighborhoodFoodMultiplier());
                    break;
                case WASTELAND:
                    processLocationAction(notification, character, action.getLocation(),
                            gameProperties.getWastelandEncounterProbability(),
                            gameProperties.getWastelandLootProbability(),
                            gameProperties.getWastelandJunkMultiplier(), gameProperties.getWastelandFoodMultiplier());
                    break;
                case CITY_CENTER:
                    processLocationAction(notification, character, action.getLocation(),
                            gameProperties.getCityEncounterProbability(), gameProperties.getCityLootProbability(),
                            gameProperties.getCityJunkMultiplier(), gameProperties.getCityFoodMultiplier());
                    break;
                case TAVERN:
                    Character hired = clanService.hireCharacter(character.getClan());

                    notificationService.addLocalizedTexts(notification.getText(), "location.tavern.joined", new String[]{character.getName(), hired.getName()});

                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.character.joined", new String[]{hired.getName()});
                    notification.getDetails().add(detail);
                    break;
                case ARENA:
                    // arena processed separately
                default:
                    log.error("Encountered unknown location: {}", action.getLocation());

            }

            // send notification about action result
            notificationService.save(notification);

            action.setState(ActionState.COMPLETED);
            locationActionRepository.save(action);
        }
    }

    @Override
    public void processArenaActions(int turnId) {
        log.debug("Executing arena actions");

        List<LocationAction> actions =
                locationActionRepository.findAllByStateAndLocation(ActionState.PENDING, Location.ARENA);
        List<Character> characters = new ArrayList<>(actions.size());

        for (LocationAction action : actions) {
            characters.add(action.getCharacter());

            // mark action as completed
            action.setState(ActionState.COMPLETED);
            locationActionRepository.save(action);
        }

        log.debug("{} characters entered arena.", characters.size());

        // process arena fights
        List<ArenaResult> results = combatService.handleArenaFights(characters);

        for (ArenaResult result : results) {
            // save results
            result.getNotification().setTurnId(turnId);
            notificationService.save(result.getNotification());
            characterService.save(result.getCharacter());
        }

        // clear arena data
        clanService.clearArenaCharacters();
    }

    private void processLocationAction(ClanNotification notification, Character character, Location location,
                                       int encounterProbability, int lootProbability, int junkRatio, int foodRatio) {
        /*
         * ENCOUNTER
         *
         * If the character encounters a random event, process it and finish exploration. The chance for an encounter
         * differs based on location type.
         */
        int encounterRoll = randomService.getRandomInt(1, RandomService.K100);
        if (encounterRoll <= encounterProbability) {
            log.debug("Random encounter triggered!");

            encounterService.handleEncounter(notification, character, location);
            return;
        }

        /*
         * LOOT
         *
         * If not encounter has been triggered, the character receives a chance to find a random loot based on the
         * location type.
         */
        lootProbability += character.getScavenge() * 5;
        int lootRoll = randomService.getRandomInt(1, RandomService.K100);
        if (lootRoll <= lootProbability) {
            log.debug("Loot generated!");

            notificationService.addLocalizedTexts(notification.getText(), "location.loot", new String[]{character.getName()});
            itemService.generateItemForCharacter(character, notification);

            return;
        }

        /*
         * JUNK AND FOOD
         *
         * If nothing from the above triggered, the character finds some junk or food at the location.
         */
        if (randomService.getRandomInt(1, 100) > 50) {
            log.debug("Junk found!");
            int junk = character.getScavenge() * junkRatio;
            Clan clan = character.getClan();
            clan.setJunk(clan.getJunk() + junk);
            clanService.saveClan(clan);

            notificationService.addLocalizedTexts(notification.getText(), "location.junk", new String[]{character.getName()});
            notification.setJunkIncome(junk);
        } else {
            log.debug("Food found!");
            int food = character.getScavenge() * foodRatio;
            Clan clan = character.getClan();
            clan.setFood(clan.getFood() + food);
            clanService.saveClan(clan);

            notificationService.addLocalizedTexts(notification.getText(), "location.food", new String[]{character.getName()});
            notification.setFoodIncome(food);
        }

    }
}
