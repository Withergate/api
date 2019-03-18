package com.withergate.api.service.action;

import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * ActionService interface.
 *
 * @author Martin Myslik
 */
public interface IActionService {

    /**
     * Creates and persists a location action based on the exploration request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the location request
     * @throws InvalidActionException
     */
    void createLocationAction(LocationRequest request, int clanId) throws InvalidActionException;

    /**
     * Creates and persists a building action based on the request. Throws an exception if this action
     * cannot be performed.
     *
     * @param request the building request
     * @throws InvalidActionException
     */
    void createBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException;

    /**
     * Executes all pending location actions.
     *
     * @param turnId turn ID
     */
    void performPendingLocationActions(int turnId);

    /**
     * Executes all pending building actions.
     *
     * @param turnId turn ID
     */
    void performPendingBuildingActions(int turnId);

    /**
     * Executes all pending arena actions.
     *
     * @param turnId turn ID
     */
    void performPendingArenaActions(int turnId);
}
