package com.withergate.api.controller;

import com.withergate.api.model.request.ResourceTradeRequest;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.exception.InvalidActionException;

import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    public TradeController(ActionService actionService) {this.actionService = actionService;}

    /**
     * Submits a new resource trade action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the trade action request
     * @throws InvalidActionException
     */
    @RequestMapping(value = "/trade/resources/action", method = RequestMethod.POST)
    public ResponseEntity<Void> submitResourceTradeAction(Principal principal, @RequestBody ResourceTradeRequest request)
            throws InvalidActionException {
        actionService.createResourceTradeAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
