package com.withergate.api.controller.location;

import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.repository.LocationDescriptionRepository;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.exception.InvalidActionException;

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
 * LocationAction controller. Enables the execution of location actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class LocationController {

    private final ActionService locationService;
    private final LocationDescriptionRepository locationDescriptionRepository;

    public LocationController(ActionService locationService,
                              LocationDescriptionRepository locationDescriptionRepository) {
        this.locationService = locationService;
        this.locationDescriptionRepository = locationDescriptionRepository;
    }

    /**
     * Submits a new location action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the location action request
     * @throws InvalidActionException
     */
    @PostMapping("/locations/action")
    public ResponseEntity<Void> visitLocation(Principal principal, @RequestBody LocationRequest request) throws InvalidActionException {
        log.debug("Executing location action for player {}", principal.getName());

        locationService.createLocationAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<LocationDescription>> getLocations() {
        return new ResponseEntity<>(locationDescriptionRepository.findAll(), HttpStatus.OK);
    }
}
