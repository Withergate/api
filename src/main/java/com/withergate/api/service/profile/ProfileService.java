package com.withergate.api.service.profile;

import java.util.List;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.request.ProfileRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Profile service.
 *
 * @author Martin Myslik
 */
public interface ProfileService {

    /**
     * Returns profile for given player ID.
     *
     * @param profileId player ID
     * @return profile
     */
    Profile getProfile(int profileId);

    /**
     * Retrieves the list of all profiles.
     *
     * @return the list containing profiles
     */
    List<Profile> getAllProfiles();

    /**
     * Retrieves the list of profiles. Supports paging and sorting.
     *
     * @param pageable pagination and sorting
     * @return the page containing profiles in the specified order
     */
    Page<Profile> getProfiles(Pageable pageable);

    /**
     * Creates new player profile. Throws exception if not valid.
     *
     * @param request profile request
     * @param playerId player ID
     * @return created player profile
     */
    Profile createProfile(ProfileRequest request, int playerId) throws EntityConflictException, ValidationException;

    /**
     * Saves the provided profile.
     *
     * @param profile profile
     * @return saved profile
     */
    Profile saveProfile(Profile profile);

    /**
     * Recalculates all rankings.
     */
    void recalculateRankings();

    /**
     * Changes profile theme. Premium feature.
     *
     * @param profileId profile ID
     * @param theme theme
     */
    void changeTheme(int profileId, String theme);

}
