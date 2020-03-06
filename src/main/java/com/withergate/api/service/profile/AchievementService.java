package com.withergate.api.service.profile;

import java.util.List;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.model.achievement.AchievementDetails;
import com.withergate.api.profile.model.achievement.AchievementType;

/**
 * Achievement service.
 *
 * @author Martin Myslik
 */
public interface AchievementService {

    /**
     * Checks achievement validity and award new achievement without conditions.
     *
     * @param profile profile
     * @param type achievement type
     */
    void checkAchievementAward(Profile profile, AchievementType type);

    /**
     * Checks achievement validity and award new achievement without conditions.
     *
     * @param profileId profile ID
     * @param type achievement type
     */
    void checkAchievementAward(int profileId, AchievementType type);

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

    /**
     * Gets all available achievements for given profile. Premium feature.
     *
     * @param profileId profile ID
     * @return available achievements
     */
    List<AchievementDetails> getAvailableAchievements(int profileId);

    /**
     * Handles all end turn achievements.
     */
    void handleEndTurnAchievements();

    /**
     * Handles all end game achievements.
     */
    void handleEndGameAchievements();

}
