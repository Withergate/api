package com.withergate.api.service.disaster;

import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.request.DisasterRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Disaster service.
 *
 * @author Martin Myslik
 */
public interface DisasterService {

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
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveDisasterAction(DisasterRequest request, int clanId) throws InvalidActionException;

    /**
     * Processes all pending disaster actions.
     *
     * @param turnId turn ID
     */
    void processDisasterActions(int turnId);

}
