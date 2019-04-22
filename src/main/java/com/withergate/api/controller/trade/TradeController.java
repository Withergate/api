package com.withergate.api.controller.trade;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.view.Views;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.trade.TradeService;

import java.security.Principal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Trade controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class TradeController {

    private final ActionService actionService;
    private final TradeService tradeService;

    public TradeController(ActionService actionService, TradeService tradeService) {
        this.actionService = actionService;
        this.tradeService = tradeService;
    }

    /**
     * Submits a new resource trade action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the trade action request
     * @throws InvalidActionException
     */
    @PostMapping("/trade/resources/action")
    public ResponseEntity<Void> submitResourceTradeAction(Principal principal, @RequestBody ResourceTradeRequest request)
            throws InvalidActionException {
        actionService.createResourceTradeAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Gets all clan items as market offers.
     *
     * @param principal the principal
     */
    @JsonView(Views.Public.class)
    @GetMapping("/trade/offers")
    public ResponseEntity<List<MarketOffer>> getClanTradeOffers(Principal principal) {
        return new ResponseEntity<>(tradeService.prepareClanMarketOffers(Integer.parseInt(principal.getName())), HttpStatus.OK);
    }

}
