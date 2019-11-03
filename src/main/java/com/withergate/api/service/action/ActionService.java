package com.withergate.api.service.action;

import com.withergate.api.model.request.ArenaRequest;
import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.request.DisasterRequest;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.model.request.MarketTradeRequest;
import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.model.request.ResearchRequest;
import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.model.request.TavernRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * ActionService interface.
 *
 * @author Martin Myslik
 */
public interface ActionService {

    /**
     * Creates and persists a location action based on the exploration request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the location request
     * @throws InvalidActionException invalid action
     */
    void createLocationAction(LocationRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists an arena action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the arena request
     * @throws InvalidActionException invalid action
     */
    void createArenaAction(ArenaRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a tavern action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the tavern request
     * @throws InvalidActionException invalid action
     */
    void createTavernAction(TavernRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a building action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the building request
     * @throws InvalidActionException invalid action
     */
    void createBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a research action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the research request
     * @throws InvalidActionException invalid action
     */
    void createResearchAction(ResearchRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a quest action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the quest request
     * @throws InvalidActionException invalid action
     */
    void createQuestAction(QuestRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a resource trade action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the resource trade request
     * @throws InvalidActionException invalid action
     */
    void createResourceTradeAction(ResourceTradeRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a market trade action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the market trade request
     * @throws InvalidActionException invalid action
     */
    void createMarketTradeAction(MarketTradeRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a disaster action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the quest request
     * @throws InvalidActionException invalid action
     */
    void createDisasterAction(DisasterRequest request, int clanId) throws InvalidActionException;

    /**
     * Executes all pending location actions.
     *
     * @param turnId turn ID
     */
    void processLocationActions(int turnId);

    /**
     * Executes all pending building actions.
     *
     * @param turnId turn ID
     */
    void processBuildingActions(int turnId);

    /**
     * Executes all pending research actions.
     *
     * @param turnId turn ID
     */
    void processResearchActions(int turnId);

    /**
     * Executes all pending quest actions.
     *
     * @param turnId turn ID
     */
    void processQuestActions(int turnId);

    /**
     * Executes all pending trade actions.
     *
     * @param turnId turn ID
     */
    void processTradeActions(int turnId);

    /**
     * Checks and handles current disaster.
     *
     * @param turnId turn ID
     */
    void processDisaster(int turnId);

    /**
     * Assing default actions to all characters.
     */
    void assignDefaultActions();

}
