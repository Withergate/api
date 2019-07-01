package com.withergate.api.service.disaster;

import com.withergate.api.model.action.DisasterAction;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterSolution;

/**
 * Disaster service.
 *
 * @author Martin Myslik
 */
public interface DisasterService {

    /**
     * Loads the current disaster.
     *
     * @return the loaded disaster
     */
    Disaster getCurrentDisaster();

    /**
     * Loads the disaster solution based on its identifier.
     *
     * @param  identifier solution identifier
     * @return the loaded disaster solution
     */
    DisasterSolution getDisasterSolution(String identifier);

    /**
     * Loads the current disaster. Checks whether the disaster is visible for the provided clan.
     *
     * @param clanId the clan ID
     * @return the loaded disaster
     */
    Disaster getDisasterForClan(int clanId);

    /**
     * Handles the disaster mechanics. Should be invoked every turn.
     *
     * @param turnId the current turn
     */
    void handleDisaster(int turnId);

    /**
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveDisasterAction(DisasterAction action);

    /**
     * Processes all pending disaster actions.
     *
     * @param turnId turn ID
     */
    void processDisasterActions(int turnId);

}
