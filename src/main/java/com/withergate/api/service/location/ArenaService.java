package com.withergate.api.service.location;

import com.withergate.api.model.action.ArenaAction;

/**
 * Arena service interface.
 *
 * @author Martin Myslik
 */
public interface ArenaService {

    /**
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveArenaAction(ArenaAction action);

    /**
     * Handles all pending arena actions.
     *
     * @param turnId turn ID
     */
    void processArenaActions(int turnId);

}
