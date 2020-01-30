package com.withergate.api.service.profile;

import java.time.LocalDate;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.profile.model.HistoricalResult;
import com.withergate.api.profile.repository.HistoricalResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
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
    private final BuildProperties buildProperties;
    private final GameProperties gameProperties;

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
                result.setPlayerId(clan.getId());
                result.setNumTurns(gameProperties.getMaxTurns());
                result.setFame(clan.getFame());
                result.setClanName(clan.getName());

                resultRepository.save(result);
            }
        } catch (Exception e) {
            log.error("Error saving historical results.", e);
        }
    }

    @Override
    public List<HistoricalResult> loadResults(int playerId) {
        return resultRepository.findAllByPlayerId(playerId);
    }
}
