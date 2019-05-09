package com.withergate.api.service.trade;

import com.withergate.api.model.action.ResourceTradeAction;

/**
 * Trade service interface.
 *
 * @author Martin Myslik
 */
public interface TradeService {

    /**
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveResourceTradeAction(ResourceTradeAction action);

    /**
     * Processes all pending resource trade actions.
     *
     * @param turnId turn ID
     */
    void processResourceTradeActions(int turnId);

}
