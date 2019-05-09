package com.withergate.api.controller.trade;

import java.security.Principal;

import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.trade.TradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ActionService actionService;
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
        actionService.createResourceTradeAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
