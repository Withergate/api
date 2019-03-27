package com.withergate.api.service.location;

import com.withergate.api.model.action.LocationAction;

/**
 * Location service interface.
 *
 * @author Martin Myslik
 */
public interface LocationService {

    /**
     * Saves the provided action
     *
     * @param action the action to be saved
     */
    void saveLocationAction(LocationAction action);

    /**
     * Handles all pending location actions.
     *
     * @param turnId turn ID
     */
    void processLocationActions(int turnId);

    /**
     * Handles all pending arena actions.
     *
     * @param turnId turn ID
     */
    void processArenaActions(int turnId);

}
