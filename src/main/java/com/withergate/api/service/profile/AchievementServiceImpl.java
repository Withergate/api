package com.withergate.api.service.profile;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.EndBonusType;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.profile.model.PremiumType;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.model.achievement.Achievement;
import com.withergate.api.profile.model.achievement.AchievementDetails;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.profile.repository.AchievementDetailsRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.premium.Premium;
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
    private final ClanService clanService;
    private final AchievementDetailsRepository detailsRepository;

    @Transactional(transactionManager = "profileTransactionManager", propagation = Propagation.REQUIRED)
    @Override
    public void handleEndTurnAchievements() {
        log.debug("--> Handling end turn achievements.");
        for (Profile profile : profileService.getAllProfiles()) {
            if (profile.getPremiumType() != null) {
                checkAchievementAward(profile, AchievementType.PREMIUM, profile.getPremiumType().name());
            }
            checkAchievementAward(profile, AchievementType.CONSECUTIVE_LOGINS, profile.getConsecutiveLogins());

            // clan achievements
            Clan clan = clanService.getClan(profile.getId());
            if (clan == null) continue;

            if (clan.getBuildings().stream().noneMatch(b -> b.getLevel() < 1)) { // all buildings level at least 1
                checkAchievementAward(profile, AchievementType.BUILDING_ALL);
            }
            for (Building building : clan.getBuildings()) {
                checkAchievementAward(profile, AchievementType.BUILDING_TOP, building.getLevel()); // building of certain level
                if (building.getDetails().getEndBonusType() != null
                        && building.getDetails().getEndBonusType().equals(EndBonusType.CLAN_DEFENSE)) { // defense building
                    checkAchievementAward(profile, AchievementType.BUILDING_DEFENSE, building.getLevel());
                }
            }
            checkAchievementAward(profile, AchievementType.RESEARCH_COUNT,
                    (int) clan.getResearch().stream().filter(Research::isCompleted).count());
            checkAchievementAward(profile, AchievementType.INFORMATION_LEVEL, clan.getInformationLevel());
            checkAchievementAward(profile, AchievementType.CHARACTER_COUNT, clan.getCharacters().size());
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
    public void checkAchievementAward(int profileId, AchievementType type, int value) {
        checkAchievementAward(profileService.getProfile(profileId), type, value);
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

    @Premium(type = PremiumType.SILVER)
    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public List<AchievementDetails> getAvailableAchievements(int profileId) {
        Profile profile = profileService.getProfile(profileId);
        Set<String> completed = new HashSet<>();
        profile.getAchievements().forEach(a -> completed.add(a.getDetails().getIdentifier()));

        List<AchievementDetails> achievementDetails = detailsRepository.findAll();
        List<AchievementDetails> filtered = achievementDetails.stream()
                .filter(d -> !completed.contains(d.getIdentifier())).collect(Collectors.toList());

        return filtered;
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
