package com.withergate.api.service.action;

import com.withergate.api.Constants;
import com.withergate.api.model.ArenaResult;
import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.WeaponType;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.IClanService;
import com.withergate.api.service.encounter.ICombatService;
import com.withergate.api.service.item.IItemService;
import com.withergate.api.service.encounter.IEncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final IClanService clanService;
    private final ICombatService combatService;

    /**
     * Constructor.
     *
     * @param characterService           character service
     * @param locationActionRepository   locationAction repository
     * @param clanNotificationRepository player notification repository
     * @param randomService              random service
     * @param encounterService           encounter service
     * @param itemService                item service
     * @param clanService                clan service
     * @param combatService              combat service
     */
    public ActionService(CharacterService characterService, LocationActionRepository locationActionRepository,
                         ClanNotificationRepository clanNotificationRepository, RandomService randomService,
                         IEncounterService encounterService, IItemService itemService, IClanService clanService,
                         ICombatService combatService) {
        this.characterService = characterService;
        this.locationActionRepository = locationActionRepository;
        this.clanNotificationRepository = clanNotificationRepository;
        this.randomService = randomService;
        this.encounterService = encounterService;
        this.itemService = itemService;
        this.clanService = clanService;
        this.combatService = combatService;
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

        // check if clan has enough resources
        if (request.getLocation() == Location.TAVERN) {
            Clan clan = character.getClan();
            if (clan.getCaps() < Constants.CHARACTER_COST) {
                throw new InvalidActionException("Not enough resources to perform this action!");
            }
            clan.setCaps(clan.getCaps() - Constants.CHARACTER_COST);
            clanService.saveClan(clan);
        }

        // check arena requirements
        if (request.getLocation() == Location.ARENA) {
            Clan clan = character.getClan();
            if (clan.isArena()) {
                throw new InvalidActionException("You already have selected a character to enter arena this turn!");
            }
            if (character.getWeapon() != null && character.getWeapon().getDetails().getType() != WeaponType.MELEE) {
                throw new InvalidActionException("Only melee weapons are allowed to the arena!");
            }
            clan.setArena(true);
            clanService.saveClan(clan);
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

            switch (action.getLocation()) {
                case NEIGHBORHOOD:
                    processLocationAction(notification, character, action.getLocation(),
                            Constants.NEIGHBORHOOD_ENCOUNTER_PROBABILITY, Constants.NEIGHBORHOOD_LOOT_PROBABILITY, Constants.NEIGHBORHOOD_JUNK_MULTIPLIER);
                    break;
                case WASTELAND:
                    processLocationAction(notification, character, action.getLocation(),
                            Constants.WASTELAND_ENCOUNTER_PROBABILITY, Constants.WASTELAND_LOOT_PROBABILITY, Constants.WASTELAND_JUNK_MULTIPLIER);
                    break;
                case CITY:
                    processLocationAction(notification, character, action.getLocation(),
                            Constants.CITY_ENCOUNTER_PROBABILITY, Constants.CITY_LOOT_PROBABILITY, Constants.CITY_JUNK_MULTIPLIER);
                    break;
                case TAVERN:
                    Character hired = clanService.hireCharacter(character.getClan());

                    notification.setText("[" + character.getName() + "] went to the tavern to hire someone for your clan. " +
                            "After spending the evening chatting with several people, the decision fell on [" + hired.getName() + "].");
                    notification.setIncome("[100] caps were deducted from your clan storage. New character was added to your clan.");
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

    @Transactional
    @Override
    public void performPendingArenaActions(int turnId) {
        log.debug("Executing arena actions");

        List<LocationAction> actions = locationActionRepository.findAllByStateAndLocation(ActionState.PENDING, Location.ARENA);
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
        notification.setIncome("Added [" + junk + "] junk to your storage.");
    }

}
