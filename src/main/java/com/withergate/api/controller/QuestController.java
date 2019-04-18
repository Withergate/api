package com.withergate.api.controller;

import com.withergate.api.model.request.QuestRequest;
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
 * BuildingAction controller. Enables the execution of building actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class QuestController {

    private final ActionService actionService;

    public QuestController(ActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * Submits a new building action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the building action request
     * @throws InvalidActionException
     */
    @RequestMapping(value = "/quest/action", method = RequestMethod.POST)
    public ResponseEntity<Void> submitQuestAction(Principal principal, @RequestBody QuestRequest request) throws InvalidActionException {
        actionService.createQuestAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}