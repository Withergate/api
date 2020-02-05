package com.withergate.api.service.profile;

import java.time.LocalDateTime;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.repository.ProfileRepository;
import com.withergate.api.profile.request.ProfileRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private static final int PROFILE_NAME_MIN_LENGTH = 6;
    private static final int PROFILE_NAME_MAX_LENGTH = 30;
    private static final int BASE_RANKING = 100;

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
            throw new ValidationException("Profile name must be between 6 and 18 characters long.");
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
        profile.setRanking(BASE_RANKING);

        return repository.save(profile);
    }

    @Transactional(transactionManager = "profileTransactionManager")
    @Override
    public Profile saveProfile(Profile profile) {
        return repository.save(profile);
    }
}
