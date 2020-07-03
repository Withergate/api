package com.withergate.api.scheduling;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.withergate.api.profile.model.Profile;
import com.withergate.api.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Update scheduler. Used for methods running once per day.
 *
 * @author Martin Myslik
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateScheduler {

    private final ProfileService profileService;

    /**
     * Processes all turn-related events at specified times.
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "UTC") // every day midnigth
    public void processUpdates() {
        // handle consecutive logins
        for (Profile profile : profileService.getAllProfiles()) {
            removeConsecutiveLogins(profile);
        }
    }

    // null consecutive logins for players that logged in last two days ago
    private void removeConsecutiveLogins(Profile profile) {
        LocalDate today = LocalDate.now();
        if (profile.getLastActivity() != null) {
            LocalDate lastDate = profile.getLastActivity().toLocalDate();
            // last activity two days before today
            if (today.minus(2, ChronoUnit.DAYS).isAfter(lastDate)) {
                profile.setConsecutiveLogins(0);
                profileService.saveProfile(profile);
            }
        }
    }

}
