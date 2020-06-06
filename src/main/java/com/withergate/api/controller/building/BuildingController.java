package com.withergate.api.controller.building;

import com.withergate.api.game.model.request.BuildingRequest;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * BuildingAction controller. Enables the execution of building actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class BuildingController {

    private final BuildingService buildingService;

    /**
     * Submits a new building action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the building action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/buildings/action")
    public ResponseEntity<Void> submitBuildingAction(Principal principal, @RequestBody BuildingRequest request)
            throws InvalidActionException {

        buildingService.saveAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
