package com.withergate.api.service.trade;

import java.util.List;

import com.withergate.api.model.action.MarketTradeAction;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.request.PublishOfferRequest;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.service.exception.InvalidActionException;

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
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveMarketTradeAction(MarketTradeAction action);

    /**
     * Gets specified market offer.
     *
     * @param offerId the offer ID
     * @return the loaded market offer
     */
    MarketOffer loadMarketOffer(int offerId);

    /**
     * Processes all pending resource trade actions.
     *
     * @param turnId turn ID
     */
    void processResourceTradeActions(int turnId);

    /**
     * Processes all pending market trade actions.
     *
     * @param turnId turn ID
     */
    void processMarketTradeActions(int turnId);

    /**
     * Publishes new market offer.
     *
     * @param request the publish request
     * @param clanId the clan ID
     * @throws InvalidActionException invalid action
     */
    void publishMarketOffer(PublishOfferRequest request, int clanId) throws InvalidActionException;

    /**
     * Deletes existing market offer.
     *
     * @param offerId the ID of the offer to be deleted
     * @param clanId the clan ID
     * @throws InvalidActionException invalid action
     */
    void deleteMarketOffer(int offerId, int clanId) throws InvalidActionException;

    /**
     * Gets all market offers by state.
     *
     * @param state the state
     * @return list of market offers
     */
    List<MarketOffer> getMarketOffersByState(State state);

    /**
     * Performs computer trade actions. If an offer is eligible for passive trade action, it will be performed in
     * scope of this method.
     *
     * @param turnId turn ID
     */
    void performComputerTradeActions(int turnId);

}
