package com.withergate.api.service.action;

import com.withergate.api.game.model.request.ActionRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Actionable interface.
 *
 * @author Martin Myslik
 */
public interface Actionable<T extends ActionRequest> {

    /**
     * Runs all actions of given type.
     *
     * @param turn turn
     */
    void runActions(int turn);

    /**
     * Saves and validates the provided action.
     *
     * @param request action request
     * @param clanId clan ID
     * @throws InvalidActionException if action did not pass validation
     */
    void saveAction(T request, int clanId) throws InvalidActionException;

    /**
     * Gets action order.
     *
     * @return action order
     */
    int getOrder();

}
