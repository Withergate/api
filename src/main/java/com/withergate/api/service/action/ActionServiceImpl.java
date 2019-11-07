package com.withergate.api.service.action;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ArenaAction;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.DisasterAction;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.action.MarketTradeAction;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.action.ResearchAction;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterSolution;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.request.ArenaRequest;
import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.request.DisasterRequest;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.model.request.MarketTradeRequest;
import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.model.request.ResearchRequest;
import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.model.request.TavernRequest;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.model.trade.TradeType;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.disaster.DisasterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.ArenaService;
import com.withergate.api.service.location.LocationService;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.research.ResearchService;
import com.withergate.api.service.trade.TradeService;
import com.withergate.api.service.trade.TradeServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * LocationAction service. Responsible for the execution of all location-bound actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class ActionServiceImpl implements ActionService {

    private final CharacterService characterService;
    private final LocationService locationService;
    private final BuildingService buildingService;
    private final ResearchService researchService;
    private final QuestService questService;
    private final TradeService tradeService;
    private final ArenaService arenaService;
    private final TavernService tavernService;
    private final DisasterService disasterService;

    @Transactional
    @Override
    public void createLocationAction(LocationRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting location action request: {}", request.toString());
        Character character = getCharacter(request.getCharacterId(), clanId);

        // check if this location supports given action type
        LocationDescription description = locationService.getLocationDescription(request.getLocation());
        if (description == null || (request.getType() == LocationAction.LocationActionType.SCOUT && !description.isScouting())) {
            throw new InvalidActionException("Location not found or does not support specified action!");
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
    public void createArenaAction(ArenaRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting arena action request: {}", request.toString());
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check arena requirements
        if (clan.isArena()) {
            throw new InvalidActionException("You already have selected a character to enter arena this turn!");
        }
        clan.setArena(true);

        ArenaAction action = new ArenaAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);

        arenaService.saveArenaAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
    }

    @Transactional
    @Override
    public void createTavernAction(TavernRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting tavern action request: {}", request.toString());
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        TavernOffer offer = tavernService.loadTavernOffer(request.getOfferId());
        if (offer == null || offer.getClan().getId() != clanId
                || !offer.getState().equals(TavernOffer.State.AVAILABLE)) {
            throw new InvalidActionException("This offer either doesn't exists or does not belong to your clan!");
        }

        int cost = offer.getPrice();
        if (clan.getCaps() < cost) {
            throw new InvalidActionException("Not enough resources to perform this action!");
        }

        if (clan.getCharacters().size() >= clan.getPopulationLimit()) {
            throw new InvalidActionException("Population limit exceeded.");
        }

        clan.changeCaps(- cost);

        TavernAction action = new TavernAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setOffer(offer);

        tavernService.saveTavernAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);

        // mark offer as hired
        offer.setState(TavernOffer.State.HIRED);
    }

    @Transactional
    @Override
    public void createBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting building action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);

        // check if action is applicable
        BuildingDetails buildingDetails = buildingService.getBuildingDetails(request.getBuilding());
        if (buildingDetails == null) {
            throw new InvalidActionException("This building does not exist");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !character.getClan().getBuildings().containsKey(request.getBuilding())) {
            throw new InvalidActionException("This building is not constructed yet!");
        }

        if (request.getType() == BuildingAction.Type.VISIT
                && character.getClan().getBuildings().get(request.getBuilding()).getLevel() < 1) {
            throw new InvalidActionException("This building has not reached sufficient level yet!!");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !character.getClan()
                .getBuildings()
                .get(request.getBuilding())
                .getDetails()
                .isVisitable()) {
            throw new InvalidActionException("This building does not support this type of action.");
        }

        if (request.getType() == BuildingAction.Type.VISIT && character.getClan().getJunk() < buildingDetails.getVisitJunkCost()) {
            throw new InvalidActionException("Not enough junk to perform this action!");
        }

        if (request.getType() == BuildingAction.Type.CONSTRUCT && character.getClan().getJunk() < character.getCraftsmanship()) {
            throw new InvalidActionException("Not enough junk to perform this action.");
        }

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(buildingDetails.getIdentifier());
        action.setType(request.getType());

        buildingService.saveBuildingAction(action);

        // pay junk
        Clan clan = character.getClan();
        if (request.getType().equals(BuildingAction.Type.CONSTRUCT)) {
            clan.changeJunk(- character.getCraftsmanship());
        } else if (request.getType().equals(BuildingAction.Type.VISIT)) {
            clan.changeJunk(- buildingDetails.getVisitJunkCost());
        }

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
    }

    @Transactional
    @Override
    public void createResearchAction(ResearchRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting building action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if research is available
        if (!clan.getResearch().containsKey(request.getResearch())) {
            throw new InvalidActionException("The specified research is not available to your clan.");
        }

        Research research = clan.getResearch().get(request.getResearch());
        if (research.isCompleted()) {
            throw new InvalidActionException("This research has been completed already.");
        }

        ResearchAction action = new ResearchAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setResearch(research.getDetails().getIdentifier());

        researchService.saveResearchAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
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
                if (q.isCompleted()) {
                    throw new InvalidActionException("This quest has already been completed.");
                }
                quest = q;
                break;
            }
        }

        if (quest == null) {
            throw new InvalidActionException("This quest does not exist.");
        }

        // persist the action
        QuestAction action = new QuestAction();
        action.setCharacter(character);
        action.setQuest(quest);
        action.setState(ActionState.PENDING);
        questService.saveQuestAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
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

        if (request.getJunk() + request.getFood() < 1) {
            throw new InvalidActionException("No resources specified!");
        }

        // check if clan has enough resources
        if (request.getType().equals(TradeType.BUY)) {
            int resourcesToBuy = request.getFood() + request.getJunk();
            int cost = resourcesToBuy * 2;
            if (clan.getCaps() < cost) {
                throw new InvalidActionException("Not enough caps!");
            }

            // pay the price and save the action
            clan.changeCaps(- cost);
        }

        if (request.getType().equals(TradeType.SELL)) {
            if (clan.getFood() < request.getFood()) {
                throw new InvalidActionException("Not enough food!");
            }
            if (clan.getJunk() < request.getJunk()) {
                throw new InvalidActionException("Not enough junk!");
            }

            // pay the price
            clan.changeFood(- request.getFood());
            clan.changeJunk(- request.getJunk());
        }

        // save the action
        tradeService.saveResourceTradeAction(action);

        // mark character as busy and save the clan
        character.setState(CharacterState.BUSY);
    }

    @Transactional
    @Override
    public void createMarketTradeAction(MarketTradeRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting market trade action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        MarketOffer offer = tradeService.loadMarketOffer(request.getOfferId());

        if (offer == null || !offer.getState().equals(State.PUBLISHED)) {
            throw new InvalidActionException("This offer is not available.");
        }

        if (clan.getId() == offer.getSeller().getId()) {
            throw new InvalidActionException("You cannot buy your own item!");
        }

        if (clan.getCaps() < offer.getPrice()) {
            throw new InvalidActionException("Not enough caps to perform this action.");
        }

        // pay caps
        clan.changeCaps(- offer.getPrice());

        // create action
        MarketTradeAction action = new MarketTradeAction();
        action.setCharacter(character);
        action.setOffer(offer);
        action.setState(ActionState.PENDING);
        tradeService.saveMarketTradeAction(action);

        // mark offer as sold
        offer.setState(State.SOLD);

        // mark character as busy and save the clan
        character.setState(CharacterState.BUSY);
    }

    @Transactional
    @Override
    public void createDisasterAction(DisasterRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting disaster action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if disaster solution belongs to the current disaster
        Disaster disaster = disasterService.getCurrentDisaster();
        DisasterSolution solution = disasterService.getDisasterSolution(request.getSolution());
        if (solution == null || !solution.getDisaster().equals(disaster.getDetails())) {
            throw new InvalidActionException("This solution either doesn't exist or doesn't belong to the current disaster.");
        }

        // check resources
        if (solution.getCapsCost() > clan.getCaps() || solution.getJunkCost() > clan.getJunk()
                || solution.getFoodCost() > clan.getFood()) {
            throw new InvalidActionException("Not enough resources to perform this action.");
        }

        // check completion
        if (clan.getDisasterProgress() >= 100) {
            throw new InvalidActionException("Your already averted this disaster!");
        }

        // create action
        DisasterAction action = new DisasterAction();
        action.setCharacter(character);
        action.setSolution(solution);
        action.setState(ActionState.PENDING);
        disasterService.saveDisasterAction(action);

        // pay resources
        clan.changeCaps(- solution.getCapsCost());
        clan.changeJunk(- solution.getJunkCost());
        clan.changeFood(- solution.getFoodCost());

        // mark character as busy and save the clan
        character.setState(CharacterState.BUSY);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable(maxAttempts = 2)
    @Override
    public void processLocationActions(int turnId) {
        log.debug("-> Processing location actions...");

        // arena actions
        arenaService.processArenaActions(turnId);

        // location actions
        locationService.processLocationActions(turnId);

        // tavern actions
        tavernService.processTavernActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processBuildingActions(int turnId) {
        log.debug("-> Processing building actions...");

        // building actions
        buildingService.processBuildingActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processResearchActions(int turnId) {
        log.debug("-> Processing research actions");

        // research actions
        researchService.processResearchActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processQuestActions(int turnId) {
        log.debug("-> Processing quest actions...");

        // quest actions
        questService.processQuestActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processTradeActions(int turnId) {
        log.debug("-> Processing trade actions...");

        // resource trade actions
        tradeService.processResourceTradeActions(turnId);

        // market trade actions
        tradeService.processMarketTradeActions(turnId);

        // perform computer trade actions
        tradeService.performComputerTradeActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Retryable
    @Override
    public void processDisaster(int turnId) {
        log.debug("-> Processing disaster actions...");

        // process disaster actions
        disasterService.processDisasterActions(turnId);

        // process disaster
        disasterService.handleDisaster(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void assignDefaultActions() {
        log.debug("Assigning default actions");

        for (Character character : characterService.loadAll()) {
            // skip characters without clan
            if (character.getClan() == null) {
                continue;
            }

            // only applicable to ready characters
            if (!character.getState().equals(CharacterState.READY)) {
                continue;
            }

            // exploration
            if (character.getClan().getDefaultAction().equals(Clan.DefaultAction.EXPLORE_NEIGHBORHOOD)) {
                character.setState(CharacterState.BUSY);

                LocationAction action = new LocationAction();
                action.setLocation(Location.NEIGHBORHOOD);
                action.setCharacter(character);
                action.setType(LocationAction.LocationActionType.VISIT);
                action.setState(ActionState.PENDING);
                locationService.saveLocationAction(action);
            }
        }
    }

    private Character getCharacter(int characterId, int clanId) throws InvalidActionException {
        Character character = characterService.load(characterId);
        if (character == null || character.getClan().getId() != clanId || character.getState() != CharacterState.READY) {
            log.error("Action cannot be performed with this character: {}!", character);
            throw new InvalidActionException("Cannot perform exploration with the specified character!");
        }

        return character;
    }

}
