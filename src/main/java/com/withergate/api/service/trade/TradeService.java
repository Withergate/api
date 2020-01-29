package com.withergate.api.service.trade;

import com.withergate.api.game.model.request.MarketTradeRequest;
import com.withergate.api.game.model.request.PublishOfferRequest;
import com.withergate.api.game.model.request.ResourceTradeRequest;
import com.withergate.api.game.model.trade.MarketOffer;
import com.withergate.api.game.model.trade.MarketOffer.State;
import com.withergate.api.service.exception.InvalidActionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Trade service interface.
 *
 * @author Martin Myslik
 */
public interface TradeService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveResourceTradeAction(ResourceTradeRequest request, int clanId) throws InvalidActionException;

    /**
     * Validates handles market trade action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void handleMarketTradeAction(MarketTradeRequest request, int clanId) throws InvalidActionException;

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
     * Gets all market offers by state. Supports pagination.
     *
     * @param state the state
     * @param pageable pagination
     * @return page with market offers
     */
    Page<MarketOffer> getMarketOffersByState(State state, Pageable pageable);

    /**
     * Performs computer trade actions. If an offer is eligible for passive trade action, it will be performed in
     * scope of this method.
     *
     * @param turnId turn ID
     */
    void performComputerTradeActions(int turnId);

    /**
     * Prepares market offers published by computer and delete the old ones.
     */
    void prepareComputerMarketOffers();

}
