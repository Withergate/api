package com.withergate.api.controller;

import com.withergate.api.model.request.BuildingRequest;
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
public class BuildingController {

    private final ActionService actionService;

    public BuildingController(ActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * Submits a new building action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the building action request
     * @throws InvalidActionException
     */
    @RequestMapping(value = "/buildings/action", method = RequestMethod.POST)
    public ResponseEntity<Void> submitBuildingAction(Principal principal, @RequestBody BuildingRequest request) throws InvalidActionException {
        actionService.createBuildingAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
