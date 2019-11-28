package com.withergate.api.controller.trade;

import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.request.MarketTradeRequest;
import com.withergate.api.model.request.PublishOfferRequest;
import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.model.view.Views;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.trade.TradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Trade controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class TradeController {

    private final TradeService tradeService;

    /**
     * Submits a new resource trade action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the trade action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/trade/resources/action")
    public ResponseEntity<Void> submitResourceTradeAction(Principal principal, @RequestBody ResourceTradeRequest request)
            throws InvalidActionException {
        tradeService.saveResourceTradeAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Creates new market offer.
     *
     * @param principal the principal
     * @param request   the publish request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/trade/market")
    public ResponseEntity<Void> publishMarketOffer(Principal principal, @RequestBody PublishOfferRequest request)
            throws InvalidActionException {
        tradeService.publishMarketOffer(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Submits market trade action.
     *
     * @param principal the principal
     * @param request   the buy request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/trade/market/action")
    public ResponseEntity<Void> submitMarketTradeAction(Principal principal, @RequestBody MarketTradeRequest request)
            throws InvalidActionException {
        tradeService.handleMarketTradeAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes existing market offer.
     *
     * @param principal the principal
     * @param offerId   the offer ID
     * @throws InvalidActionException invalid action
     */
    @DeleteMapping("/trade/market/{id}")
    public ResponseEntity<Void> deleteMarketOffer(Principal principal, @PathVariable(name = "id") int offerId)
            throws InvalidActionException {
        tradeService.deleteMarketOffer(offerId, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Gets all published market offers. Supports pagination
     *
     * @param pageable pagination
     * @return list of published market offers
     */
    @JsonView(Views.Public.class)
    @GetMapping("/trade/market")
    public Page<MarketOffer> getPublishedMarketOffers(Pageable pageable) {
        return tradeService.getMarketOffersByState(State.PUBLISHED, pageable);
    }

}
