package com.withergate.api.service.disaster;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterPenalty;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Disaster penalty service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class DisasterPenaltyServiceImpl implements DisasterPenaltyService {

    private final NotificationService notificationService;
    private final RandomService randomService;
    private final GameProperties gameProperties;

    @Override
    public void handleDisasterPenalties(Clan clan, ClanNotification notification, Disaster disaster) {
        log.debug("Computing penalties for clan {}.", clan.getId());

        int numPenalties; // number of penalties to be applied
        if (clan.getDisasterProgress() < gameProperties.getDisasterFailureThreshold()) {
            numPenalties = 3;
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getFailureText(), new String[]{});
        } else if (clan.getDisasterProgress() < gameProperties.getDisasterPartialSuccessThreshold()) {
            numPenalties = 2;
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getPartialSuccessText(), new String[]{});
        } else if (clan.getDisasterProgress() < 100) {
            numPenalties = 1;
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getPartialSuccessText(), new String[]{});
        } else {
            numPenalties = 0;
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getSuccessText(), new String[]{});

            // reward fame
            clan.setFame(clan.getFame() + disaster.getDetails().getFameReward());
            notification.setFameIncome(disaster.getDetails().getFameReward());
        }

        // handle penalties
        for (int i = 0; i < numPenalties; i++) {
            handlePenalty(clan, notification, disaster.getDetails().getPenalties().get(i));
        }
    }

    private void handlePenalty(Clan clan, ClanNotification notification, DisasterPenalty penalty) {
        switch (penalty.getPenaltyType()) {
            case ITEM_LOSS: {
                handleItemLoss(clan, notification);
                break;
            }
            case RESOURCES_LOSS: {
                handleResourceLoss(clan, notification);
                break;
            }
            case CHARACTER_INJURY: {
                hanleCharacterInjury(clan, notification);
                break;
            }
            case BUILDING_DESTRUCTION: {
                hanleBuildingDestruction(clan, notification);
                break;
            }
            default: log.error("Unknown penalty type: {}", penalty.getPenaltyType());
        }
    }

    private void handleItemLoss(Clan clan, ClanNotification notification) {
        // TODO
    }

    private void handleResourceLoss(Clan clan, ClanNotification notification) {
        // TODO
    }

    private void hanleCharacterInjury(Clan clan, ClanNotification notification) {
        // TODO
    }

    private void hanleBuildingDestruction(Clan clan, ClanNotification notification) {
        // TODO
    }

}
