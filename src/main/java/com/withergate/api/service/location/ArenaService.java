package com.withergate.api.service.location;

import com.withergate.api.model.request.ArenaRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Arena service interface.
 *
 * @author Martin Myslik
 */
public interface ArenaService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveArenaAction(ArenaRequest request, int clanId) throws InvalidActionException;

    /**
     * Handles all pending arena actions.
     *
     * @param turnId turn ID
     */
    void processArenaActions(int turnId);

}
