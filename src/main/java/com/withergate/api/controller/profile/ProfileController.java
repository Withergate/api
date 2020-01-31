package com.withergate.api.controller.profile;

import java.security.Principal;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.request.ProfileRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.profile.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Profile controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Retrieves the profile for the authenticated player.
     *
     * @param principal the principal
     * @return the profile matching the id of the authenticated user
     */
    @GetMapping("/profile")
    public ResponseEntity<Profile> getProfile(Principal principal) {
        Profile profile = profileService.getProfile(Integer.parseInt(principal.getName()));

        if (profile == null) {
            log.warn("Profile with this ID does not exist yet. It should be created first!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    /**
     * Creates a new profile for the authenticated player. If this player already has a profile, returns error status.
     *
     * @param principal   the principal
     * @param request the profile request containing necessary clan details
     * @return the created profile
     * @throws EntityConflictException entity conflict
     */
    @PostMapping("/profile")
    public ResponseEntity<Profile> createClan(Principal principal, @RequestBody ProfileRequest request)
            throws EntityConflictException, ValidationException {
        log.debug("Creating a new profile for player {}", principal.getName());

        Profile profile = profileService.createProfile(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

}
