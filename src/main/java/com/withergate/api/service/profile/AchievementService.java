package com.withergate.api.service.profile;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.model.achievement.AchievementType;

/**
 * Achievement service.
 *
 * @author Martin Myslik
 */
public interface AchievementService {

    /**
     * Handles all end turn achievements.
     */
    void handleEndTurnAchievements();

    /**
     * Checks achievement validity and award new achievement without conditions.
     *
     * @param profile profile
     * @param type achievement type
     */
    void checkAchievementAward(Profile profile, AchievementType type);

    /**
     * Checks achievement validity and award new achievement if conditions are met.
     *
     * @param profile profile
     * @param type achievement type
     * @param value required value
     */
    void checkAchievementAward(Profile profile, AchievementType type, int value);

    /**
     * Checks achievement validity and award new achievement if conditions are met.
     *
     * @param profileId profile ID
     * @param type achievement type
     * @param value required value
     */
    void checkAchievementAward(int profileId, AchievementType type, int value);

    /**
     * Checks achievement validity and award new achievement if conditions are met.
     *
     * @param profile profile
     * @param type achievement type
     * @param value required value
     */
    void checkAchievementAward(Profile profile, AchievementType type, String value);

}
