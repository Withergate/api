package com.withergate.api.service.profile;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.request.ProfileRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;

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

}
