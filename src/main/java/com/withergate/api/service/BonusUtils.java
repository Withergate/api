package com.withergate.api.service;

import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.EndBonusType;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.service.notification.NotificationService;

/**
 * Bonus utils.
 *
 * @author Martin Myslik
 */
public class BonusUtils {

    private BonusUtils() {
        // disabled
    }

    /**
     * Gets bonus for given trait and bonus type. Uses injected notification service to add relevant notification detail to the provided
     * notification.
     *
     * @param character character
     * @param bonusType bonus type to be handled
     * @param notification notification
     * @param notificationService notification service
     * @return bonus
     */
    public static int getTraitBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        // find relevant trait, update notification and compute bonus
        for (Trait trait : character.getActiveTraits()) {
            if (trait.getDetails().getBonusType().equals(bonusType)) {
                if (!trait.getDetails().isOptional() || Math.random() < 0.5) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), trait.getDetails().getBonusText(),
                            new String[] {character.getName()}, trait.getDetails().getName());
                    notification.getDetails().add(detail);

                    return trait.getDetails().getBonus();
                }
            }
        }

        return 0;
    }

    /**
     * Gets bonus for given item and bonus type. Uses injected notification service to add relevant notification detail to the provided
     * notification.
     *
     * @param character character
     * @param bonusType bonus type to be handled
     * @param notification notification
     * @param notificationService notification service
     * @return bonus
     */
    public static int getItemBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        // find relevant item
        for (Item item : character.getItems()) {
            if (item.getDetails().getBonusType().equals(bonusType)) {
                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), item.getDetails().getBonusText(), new String[] {},
                        item.getDetails().getName());
                notification.getDetails().add(detail);

                return item.getDetails().getBonus();
            }
        }

        return 0;
    }

    /**
     * Gets bonus for given building and bonus type. Uses injected notification service to add relevant notification detail to the provided
     * notification.
     *
     * @param character character
     * @param bonusType bonus type to be handled
     * @param notification notification
     * @param notificationService notification service
     * @return bonus
     */
    public static int getBuildingBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        // find relevant building, update notification and compute bonus
        for (Building building : character.getClan().getBuildings()) {
            if (building.getDetails().getBonusType() != null && building.getDetails().getBonusType().equals(bonusType)
                    && building.getLevel() > 0) {
                if (building.getDetails().getBonusText() != null) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), building.getDetails().getBonusText(),
                            new String[] {character.getName()}, building.getDetails().getName());
                    notification.getDetails().add(detail);
                }
                return building.getDetails().getBonus() * building.getLevel();
            }
        }

        return 0;
    }

    /**
     * Gets end turn bonus for given building and bonus type. Uses injected notification service to add relevant notification detail to the provided
     * notification.
     *
     * @param clan clan
     * @param bonusType bonus type to be handled
     * @param notificationService notification service
     * @param notification notification
     * @return bonus
     */
    public static int getBuildingEndBonus(Clan clan, EndBonusType bonusType, NotificationService notificationService,
                                          ClanNotification notification) {
        // find relevant building, update notification and compute bonus
        for (Building building : clan.getBuildings()) {
            if (building.getDetails().getEndBonusType() != null && building.getDetails().getEndBonusType().equals(bonusType)
                    && building.getLevel() > 0) {
                if (building.getDetails().getEndBonusText() != null) {
                    notificationService.addLocalizedTexts(notification.getText(), building.getDetails().getEndBonusText(), new String[] {},
                            building.getDetails().getName());
                }
                return building.getDetails().getEndBonus() * building.getLevel();
            }
        }

        return 0;
    }

}
