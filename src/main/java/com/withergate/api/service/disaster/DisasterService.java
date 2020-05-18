package com.withergate.api.service.disaster;

import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.request.DisasterRequest;
import com.withergate.api.service.action.Actionable;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Disaster service.
 *
 * @author Martin Myslik
 */
public interface DisasterService extends Actionable {

    /**
     * Loads the current disaster. Checks whether the disaster is visible for the provided clan.
     *
     * @param clanId the clan ID
     * @return the loaded disaster
     */
    Disaster getDisasterForClan(int clanId);

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveDisasterAction(DisasterRequest request, int clanId) throws InvalidActionException;

}
