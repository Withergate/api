package com.withergate.api.service.action;

import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.IItemService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.encounter.IEncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * LocationAction service. Responsible for the execution of all location-bound actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class ActionService implements IActionService {

    private final CharacterService characterService;
    private final LocationActionRepository locationActionRepository;
    private final ClanNotificationRepository clanNotificationRepository;
    private final RandomService randomService;
    private final IEncounterService encounterService;
    private final IItemService itemService;

    /**
     * Constructor.
     *
     * @param characterService           character service
     * @param locationActionRepository   locationAction repository
     * @param clanNotificationRepository player notification repository
     * @param randomService              random service
     * @param encounterService           encounter service
     * @param itemService item service
     */
    public ActionService(CharacterService characterService, LocationActionRepository locationActionRepository,
                         ClanNotificationRepository clanNotificationRepository, RandomService randomService,
                         IEncounterService encounterService, IItemService itemService) {
        this.characterService = characterService;
        this.locationActionRepository = locationActionRepository;
        this.clanNotificationRepository = clanNotificationRepository;
        this.randomService = randomService;
        this.encounterService = encounterService;
        this.itemService = itemService;
    }

    @Transactional
    @Override
    public void createLocationAction(LocationRequest request, int clanId) throws InvalidActionException {
        log.debug("Processing location action request: {}", request.toString());
        Character character = characterService.load(request.getCharacterId());
        if (character == null || character.getClan().getId() != clanId
                || character.getState() != CharacterState.READY) {
            log.error("Action cannot be performed with this character: {}!", character);
            throw new InvalidActionException("Cannot perform exploration with the specified character!");
        }

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(request.getLocation());

        locationActionRepository.save(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
        characterService.save(character);
    }

    @Transactional
    @Override
    public void performPendingLocationActions(int turnId) {
        log.info("Executing location actions...");
        List<LocationAction> actions = locationActionRepository.findAllByState(ActionState.PENDING);

        for (LocationAction action : actions) {
            Character character = action.getCharacter();

            log.debug("Character {} is exploring {}.", character.getName(), action.getLocation());

            // prepare notification
            ClanNotification notification = new ClanNotification();
            notification.setClanId(character.getClan().getId());
            notification.setTurnId(turnId);

            // TODO - action logic

            switch (action.getLocation()) {
                case NEIGHBORHOOD:
                    processLocationAction(notification, character, action.getLocation(), 0, -25, 1);
                    break;
                case WASTELAND:
                    processLocationAction(notification, character, action.getLocation(), 25, 0, 2);
                    break;
                case CITY:
                    processLocationAction(notification, character, action.getLocation(), 50, 20, 3);
                    break;
                default:
                    log.error("Encountered unknown location: {}", action.getLocation());

            }

            // send notification about action result
            clanNotificationRepository.save(notification);

            // mark character as ready and action as executed
            if (character.getState() != CharacterState.INJURED) {
                character.setState(CharacterState.READY);
            } else {
                // TODO - handle healing
            }

            characterService.save(character);

            action.setState(ActionState.COMPLETED);
            locationActionRepository.save(action);
        }
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
        lootProbability += character.getScavenge() * 10;
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
        notification.setText("[" + character.getName() + "] found some junk at " + location + ".");
        notification.setDetails("Added [" + junk + "] junk to your storage.");
    }

}
