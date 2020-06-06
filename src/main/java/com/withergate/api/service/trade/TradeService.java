package com.withergate.api.service.trade;

import com.withergate.api.game.model.request.MarketTradeRequest;
import com.withergate.api.game.model.request.PublishOfferRequest;
import com.withergate.api.game.model.request.ResourceTradeRequest;
import com.withergate.api.game.model.trade.MarketOffer;
import com.withergate.api.game.model.trade.MarketOffer.State;
import com.withergate.api.service.action.Actionable;
import com.withergate.api.service.exception.InvalidActionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Trade service interface.
 *
 * @author Martin Myslik
 */
public interface TradeService extends Actionable<ResourceTradeRequest> {

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

}
