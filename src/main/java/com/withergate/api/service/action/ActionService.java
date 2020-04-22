package com.withergate.api.service.action;

import java.util.List;

/**
 * ActionService interface.
 *
 * @author Martin Myslik
 */
public interface ActionService {

    /**
     * Executes all pending clan combat actions.
     *
     * @param turnId turn ID
     */
    void processClanCombatActions(int turnId);

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
     * Executes all pending crafting actions.
     *
     * @param turnId turn ID
     */
    void processCraftingActions(int turnId);

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
     * Executes all pending faction actions.
     *
     * @param turnId turn ID
     */
    void processFactionActions(int turnId);

    /**
     * Performs all actions reserved for the end of the game.
     *
     * @param turnId turn ID
     */
    void performFactionFameDistribution(int turnId, List<Integer> turns);

    /**
     * Assign default actions to all characters.
     */
    void assignDefaultActions();

}
