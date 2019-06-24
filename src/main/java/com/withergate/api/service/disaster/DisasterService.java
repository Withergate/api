package com.withergate.api.service.disaster;

import com.withergate.api.model.disaster.Disaster;

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

}
