package com.withergate.api.service.disaster;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.disaster.DisasterPenalty;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.research.Research;
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
public class DisasterResolutionServiceImpl implements DisasterResolutionService {

    private final ItemService itemService;
    private final NotificationService notificationService;
    private final RandomService randomService;
    private final GameProperties gameProperties;

    @Override
    public void handleDisasterResolution(Clan clan, ClanNotification notification, Disaster disaster) {
        log.debug("Computing penalties for clan {}.", clan.getId());

        int numPenalties; // number of penalties to be applied
        if (clan.getDisasterProgress() < gameProperties.getDisasterFailureThreshold()) {
            numPenalties = 3;
            notificationService.addLocalizedTexts(notification.getText(), "disaster.failure", new String[]{},
                    disaster.getDetails().getName());
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getFailureText(),
                    new String[]{});
        } else if (clan.getDisasterProgress() < gameProperties.getDisasterPartialSuccessThreshold()) {
            numPenalties = 2;
            notificationService.addLocalizedTexts(notification.getText(), "disaster.partialSuccess",
                    new String[]{}, disaster.getDetails().getName());
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getPartialSuccessText(),
                    new String[]{});
        } else if (clan.getDisasterProgress() < 100) {
            numPenalties = 1;
            notificationService.addLocalizedTexts(notification.getText(), "disaster.partialSuccess",
                    new String[]{}, disaster.getDetails().getName());
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getPartialSuccessText(), new String[]{});
        } else {
            numPenalties = 0;
            notificationService.addLocalizedTexts(notification.getText(), "disaster.success", new String[]{},
                    disaster.getDetails().getName());
            notificationService.addLocalizedTexts(notification.getText(), disaster.getDetails().getSuccessText(), new String[]{});

            // reward fame
            clan.changeFame(disaster.getDetails().getFameReward(), "DISASTER", notification);
        }

        log.debug("Clan finished disaster with {}% and will receive {} penalties", clan.getDisasterProgress(), numPenalties);

        // handle penalties
        for (int i = 0; i < numPenalties; i++) {
            handlePenalty(clan, notification, disaster.getDetails().getPenalties().get(i));
        }

        // update statistics
        if (clan.getDisasterProgress() < 100) {
            clan.getStatistics().setFailedDisasters(clan.getStatistics().getFailedDisasters() + 1);
        }
    }

    private void handlePenalty(Clan clan, ClanNotification notification, DisasterPenalty penalty) {
        switch (penalty.getPenaltyType()) {
            case ITEM_LOSS:
                handleItemLoss(clan, notification);
                break;
            case RESOURCES_LOSS:
                handleResourceLoss(clan, notification);
                break;
            case CHARACTER_INJURY:
                handleCharacterInjury(clan, notification);
                break;
            case BUILDING_DESTRUCTION:
                handleBuildingDestruction(clan, notification);
                break;
            case FAME_LOSS:
                handleFameLoss(clan, notification);
                break;
            case INFORMATION_LOSS:
                handleInformationLoss(clan, notification);
                break;
            case RESEARCH_LOSS:
                handleResearchLoss(clan, notification);
                break;
            default: log.error("Unknown penalty type: {}", penalty.getPenaltyType());
        }
    }

    private void handleItemLoss(Clan clan, ClanNotification notification) {
        List<Item> items = new ArrayList<>(clan.getItems());

        // consider equipped items
        for (Character character : clan.getCharacters()) {
            if (character.getWeapon() != null) {
                items.add(character.getWeapon());
            }
            if (character.getOutfit() != null) {
                items.add(character.getOutfit());
            }
            if (character.getGear() != null) {
                items.add(character.getGear());
            }
        }

        if (items.isEmpty()) {
            log.debug("No items found.");

            return;
        }

        // select random item
        Item item = items.get(randomService.getRandomInt(0, items.size() - 1));

        // delete selected item
        itemService.deleteItem(item);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.item.loss", new String[]{},
                item.getDetails().getName());
        notification.getDetails().add(detail);
    }

    private void handleResourceLoss(Clan clan, ClanNotification notification) {
        int foodLoss = Math.min(gameProperties.getDisasterResourceLoss(), clan.getFood());
        int junkLoss = Math.min(gameProperties.getDisasterResourceLoss(), clan.getJunk());

        clan.changeFood(- foodLoss);
        clan.changeJunk(- junkLoss);
        notification.changeFood(- foodLoss);
        notification.changeJunk(- junkLoss);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.resource.loss", new String[]{});
        notification.getDetails().add(detail);
    }

    private void handleFameLoss(Clan clan, ClanNotification notification) {
        clan.changeFame(- gameProperties.getDisasterFameLoss(),"DISASTER", notification);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.fame.loss", new String[]{});
        notification.getDetails().add(detail);
    }

    private void handleInformationLoss(Clan clan, ClanNotification notification) {
        clan.changeInformation(- gameProperties.getDisasterInformationLoss());
        notification.changeInformation(- gameProperties.getDisasterInformationLoss());

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.information.loss", new String[]{});
        notification.getDetails().add(detail);
    }

    private void handleCharacterInjury(Clan clan, ClanNotification notification) {
        for (Character character : clan.getCharacters()) {
            int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);

            character.changeHitpoints(- injury);

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.character.injury",
                    new String[]{character.getName(), String.valueOf(injury)});

            if (character.getHitpoints() < 1) {
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.injurydeath",
                        new String[]{character.getName()});
                notification.setDeath(true);
            }

            notification.getDetails().add(detail);

            notification.changeInjury(injury);
        }
    }

    private void handleBuildingDestruction(Clan clan, ClanNotification notification) {
        for (Building building : clan.getBuildings()) {
            if (building.getLevel() > 0 || building.getProgress() > 0) {
                building.setProgress(building.getProgress() - gameProperties.getDisasterBuildingProgressLoss());

                if (building.getProgress() < 0) {
                    if (building.getLevel() > 0) {
                        building.setLevel(building.getLevel() - 1);

                        // don't lose more progress than needed
                        int remainingProgress = - building.getProgress();
                        building.setProgress(building.getNextLevelWork() - remainingProgress);
                    } else {
                        // progress must not be negative
                        building.setProgress(0);
                    }
                }

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.building.destruction",
                        new String[]{}, building.getDetails().getName());
                notification.getDetails().add(detail);
            }
        }
    }

    private void handleResearchLoss(Clan clan, ClanNotification notification) {
        for (Research research : clan.getResearch()) {
            if (!research.isCompleted() && research.getProgress() > 0) {
                research.setProgress(0);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.research.loss",
                        new String[]{}, research.getDetails().getName());
                notification.getDetails().add(detail);
            }
        }
    }

}
