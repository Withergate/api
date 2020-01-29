package com.withergate.api.service.location;

import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Location service interface.
 *
 * @author Martin Myslik
 */
public interface LocationService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId  clan ID
     */
    void saveLocationAction(LocationRequest request, int clanId) throws InvalidActionException;

    /**
     * Handles all pending location actions.
     *
     * @param turnId turn ID
     */
    void processLocationActions(int turnId);

}
