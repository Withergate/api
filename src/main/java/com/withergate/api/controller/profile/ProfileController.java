package com.withergate.api.controller.profile;

import java.security.Principal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.game.model.dto.ProfileIntelDTO;
import com.withergate.api.game.model.view.Views;
import com.withergate.api.profile.model.HistoricalResult;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.model.achievement.AchievementDetails;
import com.withergate.api.profile.request.ProfileRequest;
import com.withergate.api.profile.request.ThemeRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.profile.AchievementService;
import com.withergate.api.service.profile.HistoricalResultsService;
import com.withergate.api.service.profile.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final HistoricalResultsService resultsService;
    private final AchievementService achievementService;

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
     * Retrieves the historical results for the authenticated player.
     *
     * @param principal the principal
     * @return the loaded historical results
     */
    @GetMapping("/profile/results")
    public ResponseEntity<Page<HistoricalResult>> getHistoricalResults(Principal principal, Pageable pageable) {
        return new ResponseEntity<>(resultsService.loadResults(Integer.parseInt(principal.getName()), pageable), HttpStatus.OK);
    }

    /**
     * Retrieves the list of all profiles. Supports paging and sorting.
     *
     * @param pageable pagination and sorting
     * @return the page containing profiles in the specified order
     */
    @JsonView(Views.Public.class)
    @GetMapping("/profiles")
    public ResponseEntity<Page<Profile>> getProfiles(Pageable pageable) {
        return new ResponseEntity<>(profileService.getProfiles(pageable), HttpStatus.OK);
    }

    /**
     * Creates a new profile for the authenticated player. If this player already has a profile, returns error status.
     *
     * @param principal   the principal
     * @param request the profile request
     * @return the created profile
     * @throws EntityConflictException entity conflict
     */
    @PostMapping("/profile")
    public ResponseEntity<Profile> createProfile(Principal principal, @RequestBody ProfileRequest request)
            throws EntityConflictException, ValidationException {
        log.debug("Creating a new profile for player {}", principal.getName());

        Profile profile = profileService.createProfile(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    /**
     * Updates an existing profile for the authenticated player..
     *
     * @param principal the principal
     * @param request the profile request
     */
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(Principal principal, @RequestBody ProfileRequest request) {
        profileService.updateProfile(Integer.parseInt(principal.getName()), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates theme.
     *
     * @param principal the principal
     * @param request the theme request
     */
    @PutMapping("/profile/theme")
    public ResponseEntity<Void> updateTheme(Principal principal, @RequestBody ThemeRequest request) {
        log.debug("Changing theme preference for player {}", principal.getName());

        profileService.changeTheme(Integer.parseInt(principal.getName()), request.getTheme());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves the list of all available achievements. Premium feature.
     *
     * @param principal principal
     * @return list of available achievements
     */
    @JsonView(Views.Public.class)
    @GetMapping("/profile/achievements/available")
    public ResponseEntity<List<AchievementDetails>> getAvailableAchievements(Principal principal) {
        return new ResponseEntity<>(achievementService.getAvailableAchievements(Integer.parseInt(principal.getName())), HttpStatus.OK);
    }

    /**
     * Compares the profile with the authenticated player.
     *
     * @param principal the principal
     * @param targetId target ID
     * @return the profile intel
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileIntelDTO> compareProfile(Principal principal, @PathVariable("id") int targetId) {
        Profile profile = profileService.getProfile(Integer.parseInt(principal.getName()));
        Profile target = profileService.getProfile(targetId);

        return new ResponseEntity<>(new ProfileIntelDTO(target, profile), HttpStatus.OK);
    }

}
