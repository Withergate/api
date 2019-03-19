package com.withergate.api.service.location;

import com.withergate.api.GameProperties;
import com.withergate.api.model.ArenaResult;
import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.encounter.ICombatService;
import com.withergate.api.service.item.ItemService;

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
    private final ClanNotificationRepository clanNotificationRepository;
    private final RandomService randomService;
    private final EncounterService encounterService;
    private final ItemService itemService;
    private final ICombatService combatService;

    public LocationService(LocationActionRepository locationActionRepository,
                           GameProperties gameProperties, ClanService clanService,
                           CharacterService characterService,
                           ClanNotificationRepository clanNotificationRepository,
                           RandomService randomService,
                           EncounterService encounterService, ItemService itemService,
                           ICombatService combatService) {
        this.locationActionRepository = locationActionRepository;
        this.gameProperties = gameProperties;
        this.clanService = clanService;
        this.characterService = characterService;
        this.clanNotificationRepository = clanNotificationRepository;
        this.randomService = randomService;
        this.encounterService = encounterService;
        this.itemService = itemService;
        this.combatService = combatService;
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
                            gameProperties.getNeighborhoodJunkMultiplier());
                    break;
                case WASTELAND:
                    processLocationAction(notification, character, action.getLocation(),
                            gameProperties.getWastelandEncounterProbability(),
                            gameProperties.getWastelandLootProbability(),
                            gameProperties.getWastelandJunkMultiplier());
                    break;
                case CITY:
                    processLocationAction(notification, character, action.getLocation(),
                            gameProperties.getCityEncounterProbability(), gameProperties.getCityLootProbability(),
                            gameProperties.getCityJunkMultiplier());
                    break;
                case TAVERN:
                    Character hired = clanService.hireCharacter(character.getClan());

                    notification.setText(
                            "[" + character.getName() + "] went to the tavern to hire someone for your clan. " +
                                    "After spending the evening chatting with several people, the decision fell on ["
                                    + hired.getName() + "].");
                    notification.setCharacterIncome("New character joined your clan.");
                    break;
                case ARENA:
                    notification.setText("[" + character.getName() + "] returned from arena.");
                default:
                    log.error("Encountered unknown location: {}", action.getLocation());

            }

            // mark character as ready if still alive
            if (character.getHitpoints() > 0) {
                character.setState(CharacterState.READY);
                characterService.save(character);
            } else {
                characterService.delete(character);
            }

            // send notification about action result
            clanNotificationRepository.save(notification);

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
            result.getCharacter().setState(CharacterState.READY);
            result.getNotification().setTurnId(turnId);
            clanNotificationRepository.save(result.getNotification());
            characterService.save(result.getCharacter());
        }

        // clear arena data
        clanService.clearArenaCharacters();
    }

    private void processLocationAction(ClanNotification notification, Character character, Location location,
                                       int encounterProbability, int lootProbability, int junkRatio) {
        // default outcome
        notification.setText("[" + character.getName() + "] returned from " + location + "empty-handed.");

        /*
         * ENCOUNTER
         *
         * If the character encounters a random event, process it and finish exploration. The chance for an encounter
         * differs based on location type.
         */
        int encounterRoll = randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE);
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
        int lootRoll = randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE);
        if (lootRoll <= lootProbability) {
            log.debug("Loot generated!");

            notification.setText("[" + character.getName() + "] found loot at " + location + ".");
            itemService.generateItemForCharacter(character, notification);

            return;
        }

        /*
         * JUNK
         *
         * If nothing from the above triggered, the character finds some junk at the location.
         */
        log.debug("Junk found!");
        int junk = character.getScavenge() * junkRatio;
        Clan clan = character.getClan();
        clan.setJunk(clan.getJunk() + junk);
        clanService.saveClan(clan);

        notification.setText("[" + character.getName() + "] found some junk at " + location + ".");
        notification.setJunkIncome(junk);
    }
}
