package com.withergate.api.controller.location;

import java.security.Principal;
import java.util.List;

import com.withergate.api.game.model.location.LocationDescription;
import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.game.model.request.TavernRequest;
import com.withergate.api.game.repository.LocationDescriptionRepository;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.LocationService;
import com.withergate.api.service.location.TavernService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * LocationAction controller. Enables the execution of location actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class LocationController {

    private final TavernService tavernService;
    private final LocationService locationService;
    private final LocationDescriptionRepository locationDescriptionRepository;

    /**
     * Submits a new location action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the location action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/locations/action")
    public ResponseEntity<Void> visitLocation(Principal principal, @RequestBody LocationRequest request) throws InvalidActionException {
        log.debug("Executing location action for player {}", principal.getName());

        locationService.saveLocationAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Submits a new tavern action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the tavern action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/tavern/action")
    public ResponseEntity<Void> visitTavern(Principal principal, @RequestBody TavernRequest request) throws InvalidActionException {
        log.debug("Executing tavern action for player {}", principal.getName());

        tavernService.saveTavernAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Fetches all locations.
     *
     * @return the list of all locations
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationDescription>> getLocations() {
        return new ResponseEntity<>(locationDescriptionRepository.findAll(), HttpStatus.OK);
    }
}
