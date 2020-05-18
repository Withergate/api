package com.withergate.api.service.location;

import com.withergate.api.game.model.request.ArenaRequest;
import com.withergate.api.service.action.Actionable;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Arena service interface.
 *
 * @author Martin Myslik
 */
public interface ArenaService extends Actionable {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveArenaAction(ArenaRequest request, int clanId) throws InvalidActionException;

}
