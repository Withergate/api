package com.withergate.api.service.location;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.LocationDescriptionRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.CombatService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Location service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationActionRepository locationActionRepository;
    private final GameProperties gameProperties;
    private final ClanService clanService;
    private final CharacterService characterService;
    private final RandomService randomService;
    private final EncounterService encounterService;
    private final ItemService itemService;
    private final CombatService combatService;
    private final NotificationService notificationService;
    private final LocationDescriptionRepository locationDescriptionRepository;

    public LocationServiceImpl(LocationActionRepository locationActionRepository,
                               GameProperties gameProperties, ClanService clanService,
                               CharacterService characterService, RandomService randomService,
                               EncounterService encounterService, ItemService itemService,
                               CombatService combatService, NotificationService notificationService, LocationDescriptionRepository locationDescriptionRepository) {
        this.locationActionRepository = locationActionRepository;
        this.gameProperties = gameProperties;
        this.clanService = clanService;
        this.characterService = characterService;
        this.randomService = randomService;
        this.encounterService = encounterService;
        this.itemService = itemService;
        this.combatService = combatService;
        this.notificationService = notificationService;
        this.locationDescriptionRepository = locationDescriptionRepository;
    }

    @Override
    public LocationDescription getLocationDescription(Location location) {
        return locationDescriptionRepository.getOne(location);
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
            notification.setHeader(character.getName());

            switch (action.getLocation()) {
                case NEIGHBORHOOD:
                    processLocationAction(notification, character, action,
                            gameProperties.getNeighborhoodEncounterProbability(), gameProperties.getNeighborhoodLootProbability(),
                            gameProperties.getNeighborhoodJunkMultiplier(), gameProperties.getNeighborhoodFoodMultiplier(),
                            0);
                    break;
                case WASTELAND:
                    processLocationAction(notification, character, action,
                            gameProperties.getWastelandEncounterProbability(),gameProperties.getWastelandLootProbability(),
                            gameProperties.getWastelandJunkMultiplier(), gameProperties.getWastelandFoodMultiplier(),
                            gameProperties.getWastelandInformationMultiplier());
                    break;
                case CITY_CENTER:
                    processLocationAction(notification, character, action,
                            gameProperties.getCityEncounterProbability(), gameProperties.getCityLootProbability(),
                            gameProperties.getCityJunkMultiplier(), gameProperties.getCityFoodMultiplier(),
                            gameProperties.getCityInformationMultiplier());
                    break;
                case TAVERN:
                    Character hired = clanService.hireCharacter(character.getClan());

                    notificationService.addLocalizedTexts(notification.getText(), "location.tavern.hired", new String[]{hired.getName()});

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
            result.getNotification().setHeader(result.getCharacter().getName());
            notificationService.save(result.getNotification());
            characterService.save(result.getCharacter());
        }

        // clear arena data
        clanService.clearArenaCharacters();
    }

    private void processLocationAction(ClanNotification notification, Character character, LocationAction action,
                                       int encounterProbability, int lootProbability, int junkRatio, int foodRatio, int informationRatio) {
        /*
         * ENCOUNTER
         *
         * If the character encounters a random event, process it and finish exploration. The chance for an encounter
         * differs based on location type.
         */
        int encounterRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (encounterRoll <= encounterProbability) {
            log.debug("Random encounter triggered!");

            encounterService.handleEncounter(notification, character, action.getLocation());
            return;
        }

        /*
         * INFORMATION
         */
        if (action.getType() == LocationAction.LocationActionType.SCOUT) {
            log.debug("Information increased.");
            int information = action.getCharacter().getIntellect() * informationRatio;

            Clan clan = character.getClan();
            clan.setInformation(clan.getInformation() + information);

            notificationService.addLocalizedTexts(notification.getText(), "location.information", new String[]{});
            notification.setInformation(information);

            // handle next level
            if (clan.getInformation() > clan.getNextLevelInformation()) {
                clan.setInformation(clan.getInformation() - clan.getNextLevelInformation());
                clan.setInformationLevel(clan.getInformationLevel() + 1);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.information.levelup", new String[]{});
                notification.getDetails().add(detail);
            }

            clanService.saveClan(clan);

            return;
        }

        /*
         * LOOT
         *
         * If not encounter has been triggered, the character receives a chance to find a random loot based on the
         * location type.
         */
        lootProbability += character.getScavenge() * 5;
        int lootRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (lootRoll <= lootProbability) {
            log.debug("Loot generated!");

            notificationService.addLocalizedTexts(notification.getText(), "location.loot", new String[]{});
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
            int junk = character.getScavenge() * junkRatio + getIncomeBonus(character, notification);
            Clan clan = character.getClan();
            clan.setJunk(clan.getJunk() + junk);
            clanService.saveClan(clan);

            notificationService.addLocalizedTexts(notification.getText(), "location.junk", new String[]{});
            notification.setJunkIncome(junk);
        } else {
            log.debug("Food found!");
            int food = character.getScavenge() * foodRatio + getIncomeBonus(character, notification);
            ;
            Clan clan = character.getClan();
            clan.setFood(clan.getFood() + food);
            clanService.saveClan(clan);

            notificationService.addLocalizedTexts(notification.getText(), "location.food", new String[]{});
            notification.setFoodIncome(food);
        }

    }

    // add bonus to found junk and food when character has certain traits
    private int getIncomeBonus(Character character, ClanNotification notification) {
        if (character.getTraits().containsKey(TraitDetails.TraitName.STRONG)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.trait.strong", new String[]{character.getName()});
            notification.getDetails().add(detail);

            return 2;
        }
        return 0;
    }
}
