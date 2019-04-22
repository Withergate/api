package com.withergate.api.service.trade;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.trade.MarketOffer;

import java.util.List;

/**
 * Trade service interface.
 *
 * @author Martin Myslik
 */
public interface TradeService {

    /**
     * Saves the provided action
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

    /**
     * Prepares market offers from all available clan items.
     *
     * @param clanId the provided clan's id
     * @return the list of available market offers
     */
    List<MarketOffer> prepareClanMarketOffers(int clanId);

}
