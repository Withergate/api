package com.withergate.api.service.disaster;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterPenalty;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.item.ItemService;
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

    private final ItemService itemService;
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

        log.debug("Clan finished disaster with {}% and will receive {} penalties", clan.getDisasterProgress(), numPenalties);

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
            case FAME_LOSS: {
                handleFameLoss(clan, notification);
                break;
            }
            default: log.error("Unknown penalty type: {}", penalty.getPenaltyType());
        }
    }

    private void handleItemLoss(Clan clan, ClanNotification notification) {
        List<Item> items = new ArrayList<>();

        items.addAll(clan.getWeapons());
        items.addAll(clan.getOutfits());
        items.addAll(clan.getGear());
        items.addAll(clan.getConsumables());

        if (items.isEmpty()) {
            log.debug("No items found.");

            return;
        }

        // select random item
        Item item = items.get(randomService.getRandomInt(0, items.size() - 1));

        // delete selected item
        item.setClan(null);
        itemService.deleteItem(item);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.item.loss", new String[]{});
        notification.getDetails().add(detail);
    }

    private void handleResourceLoss(Clan clan, ClanNotification notification) {
        int foodLoss = Math.min(gameProperties.getDisasterResourceLoss(), clan.getFood());
        int junkLoss = Math.min(gameProperties.getDisasterResourceLoss(), clan.getJunk());

        clan.setFood(clan.getFood() - foodLoss);
        clan.setJunk(clan.getJunk() - junkLoss);
        notification.setFoodIncome(-foodLoss);
        notification.setJunkIncome(-junkLoss);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.resource.loss", new String[]{});
        notification.getDetails().add(detail);
    }

    private void handleFameLoss(Clan clan, ClanNotification notification) {
        clan.setFame(clan.getFame() - gameProperties.getDisasterFameLoss());
        notification.setFameIncome(notification.getFameIncome() - gameProperties.getDisasterFameLoss());

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.fame.loss", new String[]{});
        notification.getDetails().add(detail);
    }

    private void hanleCharacterInjury(Clan clan, ClanNotification notification) {
        for (Character character : clan.getCharacters()) {
            int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);

            character.setHitpoints(character.getHitpoints() - injury);

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.character.injury",
                    new String[]{character.getName(), String.valueOf(injury)});

            if (character.getHitpoints() < 1) {
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.injurydeath",
                        new String[]{character.getName()});
            }

            notification.getDetails().add(detail);

            notification.setInjury(notification.getInjury() + injury);
        }
    }

    private void hanleBuildingDestruction(Clan clan, ClanNotification notification) {
        for (Building building : clan.getBuildings().values()) {
            // lose progress
            building.setProgress(0);

            if (building.getLevel() > 0) {
                building.setLevel(building.getLevel() - 1);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.building.destruction",
                        new String[]{}, building.getDetails().getName());
                notification.getDetails().add(detail);
            }
        }
    }

}
