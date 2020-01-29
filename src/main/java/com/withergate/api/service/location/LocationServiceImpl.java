package com.withergate.api.service.location;

import com.withergate.api.game.model.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.ResearchBonusType;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.LocationAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.location.LocationDescription;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.repository.LocationDescriptionRepository;
import com.withergate.api.game.repository.action.LocationActionRepository;
import com.withergate.api.service.BonusUtils;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Location service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationActionRepository locationActionRepository;
    private final RandomService randomService;
    private final EncounterService encounterService;
    private final ItemService itemService;
    private final NotificationService notificationService;
    private final LocationDescriptionRepository locationDescriptionRepository;
    private final CharacterService characterService;

    @Transactional
    @Override
    public void saveLocationAction(LocationRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting location action request: {}", request.toString());
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);

        // check if this location supports given action type
        LocationDescription description = locationDescriptionRepository.getOne(request.getLocation());
        if (request.getType() == LocationAction.LocationActionType.SCOUT && !description.isScouting()) {
            throw new InvalidActionException("Location not found or does not support specified action!");
        }

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(request.getLocation());
        action.setType(request.getType());

        locationActionRepository.save(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
    }

    @Override
    public void processLocationActions(int turnId) {
        log.info("Executing location actions...");
        List<LocationAction> actions = locationActionRepository.findAllByState(ActionState.PENDING);

        for (LocationAction action : actions) {
            Character character = action.getCharacter();

            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
            notification.setHeader(character.getName());
            notification.setImageUrl(character.getImageUrl());

            // process action
            processLocationAction(notification, action);

            // send notification about action result
            notificationService.save(notification);

            action.setState(ActionState.COMPLETED);
        }
    }

    private void processLocationAction(ClanNotification notification, LocationAction action) {
        Character character = action.getCharacter();
        LocationDescription description = locationDescriptionRepository.getOne(action.getLocation());

        // handle random encounter first
        boolean encounter = false;
        boolean encounterSuccess = false;
        int encounterRoll = randomService.getRandomInt(1, RandomServiceImpl.K100) + character.getIntellect();
        if (description.getEncounterChance() > 0) {
            encounterRoll += BonusUtils.getBonus(character, BonusType.CAMOUFLAGE, notification, notificationService);
        }
        if (description.getEncounterChance() > 0 && encounterRoll <= description.getEncounterChance()) {
            log.debug("Random encounter triggered!");

            // handle encounter, experience is handled by encounter service
            encounterSuccess = encounterService.handleEncounter(notification, character, action.getLocation());
            encounter = true;

            if (encounterSuccess) {
                character.changeExperience(2);
                notification.changeExperience(2);
            } else {
                character.changeExperience(1);
                notification.changeExperience(1);
            }
        }

        // add experience for exploration if encounter not triggered
        if (!encounter) {
            character.changeExperience(1);
            notification.changeExperience(1);
        }

        // unsuccessful encounter will terminate the action
        if ((encounter && !encounterSuccess) || character.getHitpoints() < 1) {
            return;
        }

        // action result
        switch (action.getType()) {
            case VISIT:
                handleSearchResult(notification, character, description, encounter);
                break;
            case SCOUT:
                handleScoutResult(notification, character, description, encounter);
                break;
            default: log.error("Unknown location action type: {}", action.getType());
        }
    }

    private void handleSearchResult(ClanNotification notification, Character character, LocationDescription description,
                                    boolean encounter) {
        // loot
        int lootProbability = description.getItemChance() + character.getScavenge()
                + getLootBonus(character, description.getLocation());
        int lootRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (lootRoll <= lootProbability) {
            notificationService.addLocalizedTexts(notification.getText(), "location.loot", new String[] {});
            itemService.generateItemForCharacter(character, notification);
        }

        // junk and food
        Clan clan = character.getClan();
        notificationService.addLocalizedTexts(notification.getText(), "location.resources", new String[] {});

        int junk = character.getScavenge() + description.getJunkBonus()
                + BonusUtils.getBonus(character, BonusType.SCAVENGE_JUNK, notification, notificationService);

        // decrease junk when encounter triggered
        if (encounter) {
            junk = junk / 2;
        }

        clan.changeJunk(junk);
        notification.changeJunk(junk);

        int food = character.getScavenge() + description.getFoodBonus()
                + BonusUtils.getBonus(character, BonusType.SCAVENGE_FOOD, notification, notificationService);

        // decrease food when encounter triggered
        if (encounter) {
            food = food / 2;
        }

        clan.changeFood(food);
        notification.changeFood(food);
    }

    private void handleScoutResult(ClanNotification notification, Character character, LocationDescription description,
                                    boolean encounter) {
        log.debug("Information increased.");
        int information = character.getIntellect() + description.getInformationBonus()
                + getScoutingBonus(character, notification, encounter);

        // decrease information when encounter triggered
        if (encounter) {
            information = information / 2;
        }

        Clan clan = character.getClan();
        clan.changeInformation(information);

        notificationService.addLocalizedTexts(notification.getText(), "location.information", new String[] {});
        notification.changeInformation(information);
    }

    private int getScoutingBonus(Character character, ClanNotification notification, boolean encounter) {
        int bonus = 0;

        // trait
        bonus += BonusUtils.getBonus(character, BonusType.SCOUTING, notification, notificationService);

        // research side effect
        Research research = character.getClan().getResearch(ResearchBonusType.SCOUT_FOOD);
        if (research != null && research.isCompleted()) {
            int food = randomService.getRandomInt(1, RandomServiceImpl.K4);
            if (encounter) {
                food = food / 2;
            }
            if (food > 0) {
                character.getClan().changeFood(food);
                notification.changeFood(food);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), research.getDetails().getBonusText(), new String[]{});
                notification.getDetails().add(detail);
            }
        }

        return bonus;
    }

    private int getLootBonus(Character character, Location location) {
        Research research = character.getClan().getResearch(ResearchBonusType.NEIGHBORHOOD_LOOT);
        if (research != null && research.isCompleted() && location.equals(Location.NEIGHBORHOOD)) {
            return research.getDetails().getValue();
        }

        return 0;
    }
}
