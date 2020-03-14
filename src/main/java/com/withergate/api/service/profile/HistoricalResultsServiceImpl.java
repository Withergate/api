package com.withergate.api.service.profile;

import java.time.LocalDate;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.quest.Quest;
import com.withergate.api.game.model.statistics.ClanTurnStatistics;
import com.withergate.api.profile.model.HistoricalResult;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.profile.repository.HistoricalResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Results service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HistoricalResultsServiceImpl implements HistoricalResultsService {

    private final HistoricalResultRepository resultRepository;
    private final ProfileService profileService;
    private final BuildProperties buildProperties;
    private final GameProperties gameProperties;
    private AchievementService achievementService;

    @Autowired
    public void setAchievementService(@Lazy AchievementService achievementService) {
        this.achievementService = achievementService;
    }


    @Transactional(transactionManager = "profileTransactionManager")
    @Retryable
    @Override
    public void saveResults(List<Clan> clans) {
        log.debug("-> Persisting historical results.");
        try {
            for (int i = 0; i < clans.size(); i++) {
                Clan clan = clans.get(i);

                HistoricalResult result = new HistoricalResult();
                result.setGameVersion(buildProperties.getVersion());
                result.setGameEnded(LocalDate.now());
                result.setPlace(i + 1);
                result.setNumClans(clans.size());
                result.setProfile(profileService.getProfile(clan.getId()));
                result.setNumTurns(gameProperties.getMaxTurns());
                result.setFame(clan.getFame());
                result.setClanName(clan.getName());

                // statistics
                ClanTurnStatistics statistics = new ClanTurnStatistics(clan, 0);
                result.setFaction(clan.getFaction() != null ? clan.getFaction().getIdentifier() : null);
                result.setBuildings(statistics.getBuildings());
                result.setResearch(statistics.getResearch());
                result.setCompletedQuests((int) clan.getQuests().stream().filter(Quest::isCompleted).count());

                resultRepository.save(result);

                // handle achievements
                handleAchievements(clan, result);
            }
        } catch (Exception e) {
            log.error("Error saving historical results.", e);
        }
    }

    @Override
    public List<HistoricalResult> loadResults(int profileId) {
        return resultRepository.findAllByProfileId(profileId);
    }

    @Override
    public Page<HistoricalResult> loadResults(int playerId, Pageable pageable) {
        return resultRepository.findAllByProfileId(playerId, pageable);
    }

    private void handleAchievements(Clan clan, HistoricalResult result) {
        if (result.getPlace() == 1) {
            achievementService.checkAchievementAward(clan.getId(), AchievementType.GAME_WINNER);
        }
    }
}
