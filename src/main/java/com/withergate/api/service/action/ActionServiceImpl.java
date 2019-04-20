package com.withergate.api.service.action;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.WeaponType;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.model.trade.TradeType;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.LocationService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.trade.TradeService;
import com.withergate.api.service.trade.TradeServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LocationAction service. Responsible for the execution of all location-bound actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class ActionServiceImpl implements ActionService {

    private final CharacterService characterService;
    private final LocationService locationService;
    private final ClanService clanService;
    private final GameProperties gameProperties;
    private final BuildingService buildingService;
    private final QuestService questService;
    private final TradeService tradeService;

    public ActionServiceImpl(CharacterService characterService, LocationService locationService,
                             ClanService clanService,
                             GameProperties gameProperties, BuildingService buildingService,
                             QuestService questService, TradeService tradeService) {
        this.characterService = characterService;
        this.locationService = locationService;
        this.clanService = clanService;
        this.gameProperties = gameProperties;
        this.buildingService = buildingService;
        this.questService = questService;
        this.tradeService = tradeService;
    }

    @Transactional
    @Override
    public void createLocationAction(LocationRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting location action request: {}", request.toString());
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if clan has enough resources and population limit
        if (request.getLocation() == Location.TAVERN) {

            if (clan.getCaps() < gameProperties.getCharacterCost()) {
                throw new InvalidActionException("Not enough resources to perform this action!");
            }

            if (clan.getCharacters().size() >= clan.getPopulationLimit()) {
                throw new InvalidActionException("Population limit exceeded.");
            }

            clan.setCaps(clan.getCaps() - gameProperties.getCharacterCost());
        }

        // check if this location supports given action type
        LocationDescription description = locationService.getLocationDescription(request.getLocation());
        if (description == null || (request.getType() == LocationAction.LocationActionType.SCOUT && !description.isScouting())) {
            throw new InvalidActionException("Location not found or does not support specified action!");
        }

        // check arena requirements
        if (request.getLocation() == Location.ARENA) {
            if (clan.isArena()) {
                throw new InvalidActionException("You already have selected a character to enter arena this turn!");
            }
            if (character.getWeapon() != null && character.getWeapon().getDetails().getType() != WeaponType.MELEE) {
                throw new InvalidActionException("Only melee weapons are allowed to the arena!");
            }
            clan.setArena(true);
        }

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(request.getLocation());
        action.setType(request.getType());

        locationService.saveLocationAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
        characterService.save(character);
    }

    @Transactional
    @Override
    public void createBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting building action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if action is applicable
        BuildingDetails buildingDetails = buildingService.getBuildingDetails(request.getBuilding());
        if (buildingDetails == null) {
            throw new InvalidActionException("This building does not exist");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !character.getClan().getBuildings()
                .containsKey(request.getBuilding())) {
            throw new InvalidActionException("This building is not constructed yet!");
        }

        if (request.getType() == BuildingAction.Type.VISIT && character.getClan().getBuildings()
                .get(request.getBuilding()).getLevel() < 1) {
            throw new InvalidActionException("This building has not reached sufficient level yet!!");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !character.getClan().getBuildings()
                .get(request.getBuilding()).getDetails().isVisitable()) {
            throw new InvalidActionException("This building does not support this type of action.");
        }

        if (request.getType() == BuildingAction.Type.VISIT && clan.getJunk() < buildingDetails.getVisitJunkCost()) {
            throw new InvalidActionException("Not enough junk to perform this action!");
        }

        if (request.getType() == BuildingAction.Type.CONSTRUCT && character.getClan().getJunk() < character
                .getCraftsmanship()) {
            throw new InvalidActionException("Not enough junk to perform this action.");
        }

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(buildingDetails.getIdentifier());
        action.setType(request.getType());

        buildingService.saveBuildingAction(action);

        // pay junk
        if (request.getType().equals(BuildingAction.Type.CONSTRUCT)) {
            clan.setJunk(clan.getJunk() - character.getCraftsmanship());
        } else if (request.getType().equals(BuildingAction.Type.VISIT)) {
            clan.setJunk(clan.getJunk() - buildingDetails.getVisitJunkCost());
        }
        clanService.saveClan(clan);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
        characterService.save(character);
    }

    @Transactional
    @Override
    public void createQuestAction(QuestRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting quest action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if the clan contains this quest
        Quest quest = null;
        for (Quest q : clan.getQuests()) {
            if (q.getId() == request.getQuestId()) {
                // check if not completed
                if (q.isCompleted()) throw new InvalidActionException("This quest has already been completed.");
                quest = q;
                break;
            }
        }

        if (quest == null) throw new InvalidActionException("This quest does not exist.");

        // persist the action
        QuestAction action = new QuestAction();
        action.setCharacter(character);
        action.setQuest(quest);
        action.setState(ActionState.PENDING);
        questService.saveQuestAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
        characterService.save(character);
    }

    @Transactional
    @Override
    public void createResourceTradeAction(ResourceTradeRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting resource trade action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        ResourceTradeAction action = new ResourceTradeAction();
        action.setCharacter(character);
        action.setType(request.getType());
        action.setFood(request.getFood());
        action.setJunk(request.getJunk());
        action.setState(ActionState.PENDING);

        // check resource limit
        if (request.getJunk() + request.getFood() > TradeServiceImpl.RESOURCE_TRADE_LIMIT) {
            throw new InvalidActionException("Your character cannot carry that much!");
        }

        // check if clan has enough resources
        if (request.getType().equals(TradeType.BUY)) {
            int resourcesToBuy = request.getFood() + request.getJunk();
            int cost = resourcesToBuy * 2;
            if (clan.getCaps() < cost) {
                throw new InvalidActionException("Not enough caps!");
            }

            // pay the price and save the action
            clan.setCaps(clan.getCaps() - cost);
        }

        if (request.getType().equals(TradeType.SELL)) {
            if (clan.getFood() < request.getFood()) {
                throw new InvalidActionException("Not enough food!");
            }
            if (clan.getJunk() < request.getJunk()) {
                throw new InvalidActionException("Not enough junk!");
            }

            // pay the price
            clan.setFood(clan.getFood() - request.getFood());
            clan.setJunk(clan.getJunk() - request.getJunk());
        }

        // save the action
        tradeService.saveResourceTradeAction(action);

        // mark character as busy and save the clan
        character.setState(CharacterState.BUSY);
        clanService.saveClan(clan);
    }

    @Transactional
    @Override
    public void processLocationActions(int turnId) {
        // arena actions
        locationService.processArenaActions(turnId);

        // location actions
        locationService.processLocationActions(turnId);
    }

    @Transactional
    @Override
    public void processBuildingActions(int turnId) {
        // passive building bonuses
        buildingService.processPassiveBuildingBonuses(turnId);

        // building actions
        buildingService.processBuildingActions(turnId);
    }

    @Override
    public void processQuestActions(int turnId) {
        // quest actions
        questService.processQuestActions(turnId);
    }

    @Override
    public void processTradeActions(int turnId) {
        // resource trade actions
        tradeService.processResourceTradeActions(turnId);
    }

    private Character getCharacter(int characterId, int clanId) throws InvalidActionException {
        Character character = characterService.load(characterId);
        if (character == null || character.getClan().getId() != clanId
                || character.getState() != CharacterState.READY) {
            log.error("Action cannot be performed with this character: {}!", character);
            throw new InvalidActionException("Cannot perform exploration with the specified character!");
        }

        return character;
    }

}
