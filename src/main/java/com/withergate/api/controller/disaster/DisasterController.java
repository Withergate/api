package com.withergate.api.controller.disaster;

import java.security.Principal;

import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.request.DisasterRequest;
import com.withergate.api.service.disaster.DisasterService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Disaster controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class DisasterController {

    private final DisasterService disasterService;

    /**
     * Retrieves the current disaster for clan.
     *
     * @param principal the principal
     * @return the current disaster if visible
     */
    @GetMapping("/disaster")
    public ResponseEntity<Disaster> getClan(Principal principal) {
        Disaster disaster = disasterService.getDisasterForClan(Integer.parseInt(principal.getName()));

        return new ResponseEntity<>(disaster, HttpStatus.OK);
    }

    /**
     * Submits a new disaster action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the disaster action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/disaster/action")
    public ResponseEntity<Void> submitDisasterAction(Principal principal, @RequestBody DisasterRequest request)
            throws InvalidActionException {
        disasterService.saveAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
