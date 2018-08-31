package com.withergate.api.controller;

import com.withergate.api.model.LocationDescription;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.repository.LocationDescriptionRepository;
import com.withergate.api.service.action.IActionService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * LocationAction controller. Enables the execution of location actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class LocationController {

    private final IActionService locationService;
    private final LocationDescriptionRepository locationDescriptionRepository;

    /**
     * Constructor.
     *
     * @param locationService               location service
     * @param locationDescriptionRepository location description repository
     */
    public LocationController(IActionService locationService,
                              LocationDescriptionRepository locationDescriptionRepository) {
        this.locationService = locationService;
        this.locationDescriptionRepository = locationDescriptionRepository;
    }

    /**
     * Submits a new location action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the location action request
     */
    @RequestMapping(value = "/locations/action", method = RequestMethod.POST)
    public ResponseEntity<Void> visitLocation(Principal principal, @RequestBody LocationRequest request) {
        log.debug("Executing location action for player {}", principal.getName());

        try {
            locationService.createLocationAction(request, Integer.parseInt(principal.getName()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidActionException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public ResponseEntity<List<LocationDescription>> getLocations() {
        return new ResponseEntity<>(locationDescriptionRepository.findAll(), HttpStatus.OK);
    }
}
