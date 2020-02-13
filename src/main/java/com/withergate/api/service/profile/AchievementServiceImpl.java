package com.withergate.api.service.profile;

import java.time.LocalDate;
import java.util.List;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.model.achievement.Achievement;
import com.withergate.api.profile.model.achievement.AchievementDetails;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.profile.repository.AchievementDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Achievement service implementaiton.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AchievementServiceImpl implements AchievementService {

    private final ProfileService profileService;
    private final AchievementDetailsRepository detailsRepository;

    @Transactional(transactionManager = "profileTransactionManager", propagation = Propagation.REQUIRED)
    @Override
    public void handleEndTurnAchievements() {
        log.debug("--> Handling end turn achievements.");
        for (Profile profile : profileService.getAllProfiles()) {
            if (profile.getPremiumType() != null) {
                checkAchievementAward(profile, AchievementType.PREMIUM, profile.getPremiumType().name());
                checkAchievementAward(profile, AchievementType.CONSECUTIVE_LOGINS, profile.getConsecutiveLogins());
            }
        }
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public void checkAchievementAward(Profile profile, AchievementType type) {
        List<AchievementDetails> detailsList = detailsRepository.findAllByType(type);

        for (AchievementDetails details : detailsList) {
            awardAchievement(profile, details);
        }
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public void checkAchievementAward(Profile profile, AchievementType type, int value) {
        List<AchievementDetails> detailsList = detailsRepository.findAllByType(type);

        for (AchievementDetails details : detailsList) {
            if (value >= details.getValueNumber()) {
                awardAchievement(profile, details);
            }
        }
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public void checkAchievementAward(Profile profile, AchievementType type, String value) {
        List<AchievementDetails> detailsList = detailsRepository.findAllByType(type);

        for (AchievementDetails details : detailsList) {
            if (value.equals(details.getValueString())) {
                awardAchievement(profile, details);
            }
        }
    }

    private void awardAchievement(Profile profile, AchievementDetails details) {
        // check if this achievements exists for player
        if (profile.getAchievements().stream().anyMatch(a -> a.getDetails().getIdentifier().equals(details.getIdentifier()))) {
            return;
        }

        log.debug("Awarding achievement {} to player {}.", details.getIdentifier(), profile.getName());

        Achievement achievement = new Achievement();
        achievement.setProfile(profile);
        achievement.setDetails(details);
        achievement.setDate(LocalDate.now());
        profile.getAchievements().add(achievement);
    }
}
