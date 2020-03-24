package com.withergate.api.game.model.dto;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.model.achievement.Achievement;
import com.withergate.api.profile.model.achievement.AchievementDetails;
import lombok.Getter;

/**
 * Profile intel DTO.
 *
 * @author Martin Myslik
 */
@Getter
public class ProfileIntelDTO {

    private String name;
    private Set<AchievementDetails> extraAchievements;
    private Set<AchievementDetails> missingAchievements;

    /**
     * Constructor.
     */
    public ProfileIntelDTO(Profile target, Profile spy) {
        name = target.getName();

        extraAchievements = new HashSet<>();
        missingAchievements = new HashSet<>();

        Set<AchievementDetails> spyAchievements = new HashSet<>();
        for (Achievement achievement : spy.getAchievements()) {
            spyAchievements.add(achievement.getDetails());
        }
        Set<AchievementDetails> targetAchievements = new HashSet<>();
        for (Achievement achievement : target.getAchievements()) {
            targetAchievements.add(achievement.getDetails());
        }

        // calculate
        for (AchievementDetails details : spyAchievements) {
            Optional<AchievementDetails> option = targetAchievements.stream()
                    .filter(a -> a.getIdentifier().equals(details.getIdentifier())).findFirst();
            if (option.isEmpty()) {
                extraAchievements.add(details);
            }
        }
        for (AchievementDetails details : targetAchievements) {
            Optional<AchievementDetails> option = spyAchievements.stream()
                    .filter(a -> a.getIdentifier().equals(details.getIdentifier())).findFirst();
            if (option.isEmpty()) {
                missingAchievements.add(details);
            }
        }
    }

}
