package com.withergate.api.service.profile;

import java.time.LocalDateTime;

import com.withergate.api.profile.model.HistoricalResult;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.repository.ProfileRepository;
import com.withergate.api.profile.request.ProfileRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Profile service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private static final int PROFILE_NAME_MIN_LENGTH = 5;
    private static final int PROFILE_NAME_MAX_LENGTH = 20;
    private static final int RANKING_GAME_BONUS = 10;
    private static final String DEFAULT_THEME = "light";

    private final ProfileRepository repository;

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public Profile getProfile(int profileId) {
        return repository.findById(profileId).orElse(null);
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public Profile createProfile(ProfileRequest request, int playerId) throws EntityConflictException, ValidationException {
        // validate profile name
        if (request.getName().length() < PROFILE_NAME_MIN_LENGTH || request.getName().length() > PROFILE_NAME_MAX_LENGTH) {
            throw new ValidationException("Profile name must be between 5 and 20 characters long.");
        }

        // check if profile exists
        Profile check = repository.findOneByName(request.getName());
        if (check != null) {
            throw new EntityConflictException("Profile with this name already exists!");
        }

        Profile profile = new Profile();
        profile.setId(playerId);
        profile.setName(request.getName());
        profile.setLastActivity(LocalDateTime.now());
        profile.setRanking(0);
        profile.setTheme(DEFAULT_THEME);

        return repository.save(profile);
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public Profile saveProfile(Profile profile) {
        return repository.save(profile);
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Retryable
    @Override
    public void recalculateRankings() {
        log.debug("-> Recalculating all rankings");
        for (Profile profile : repository.findAll()) {
            if (profile.getResults().isEmpty()) continue;

            int numGames = profile.getResults().size();
            int sumFame = 0;
            for (HistoricalResult result : profile.getResults()) {
                sumFame += result.getFame();
            }

            // ranking is average score from all games plus small amount of points per played game
            double ranking = (double) (sumFame) / numGames + numGames * RANKING_GAME_BONUS;
            profile.setRanking((int) ranking);
        }
    }
}
