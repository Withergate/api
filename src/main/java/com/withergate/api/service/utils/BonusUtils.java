package com.withergate.api.service.utils;

import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
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
     * Gets bonus for given character and bonus type. Uses injected notification service to add relevant notification
     * detail to the provide notification.
     *
     * @param character character
     * @param bonusType bonus type to be handled
     * @param notification notification
     * @param notificationService notification service
     * @return bonus
     */
    public static int getBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        int traitBonus = getTraitBonus(character, bonusType, notification, notificationService);
        int itemBonus = getItemBonus(character, bonusType, notification, notificationService);
        int buildingBonus = getBuildingBonus(character, bonusType, notification, notificationService);

        return traitBonus + itemBonus + buildingBonus;
    }

    /**
     * Gets end turn bonus for given building and bonus type. Uses injected notification service to add relevant notification detail to
     * the provided notification.
     *
     * @param clan clan
     * @param bonusType bonus type to be handled
     * @param notificationService notification service
     * @param notification notification
     * @return bonus
     */
    public static int getBuildingEndBonus(Clan clan, PassiveBonusType bonusType, NotificationService notificationService,
                                          ClanNotification notification) {
        // find relevant building, update notification and compute bonus
        for (Building building : clan.getBuildings()) {
            if (building.getDetails().getPassiveBonusType() != null && building.getDetails().getPassiveBonusType().equals(bonusType)
                    && building.getLevel() > 0) {
                if (building.getDetails().getPassiveBonusText() != null) {
                    notificationService.addLocalizedTexts(notification.getText(), building.getDetails().getPassiveBonusText(), new String[] {},
                            building.getDetails().getName());
                }
                return building.getDetails().getPassiveBonus() * building.getLevel();
            }
        }

        return 0;
    }

    private static int getTraitBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        // find relevant trait, update notification and compute bonus
        for (Trait trait : character.getActiveTraits()) {
            if (trait.getDetails().getBonusType() != null && trait.getDetails().getBonusType().equals(bonusType)) {
                if (!trait.getDetails().isOptional() || Math.random() < 0.5) {
                    if (notification != null) {
                        NotificationDetail detail = new NotificationDetail();
                        notificationService.addLocalizedTexts(detail.getText(), trait.getDetails().getBonusText(),
                                new String[] {character.getName()}, trait.getDetails().getName());
                        notification.getDetails().add(detail);
                    }

                    return trait.getDetails().getBonus();
                }
            }
        }

        return 0;
    }

    private static int getItemBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        int bonus = 0;

        // find relevant item
        for (Item item : character.getItems()) {
            if (item.getDetails().getBonusType() != null && item.getDetails().getBonusType().equals(bonusType)) {
                if (notification != null) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), item.getDetails().getBonusText(), new String[] {},
                            item.getDetails().getName());
                    notification.getDetails().add(detail);
                }

                bonus += item.getDetails().getBonus();
            }
        }

        return bonus;
    }

    private static int getBuildingBonus(Character character, BonusType bonusType, ClanNotification notification,
                                    NotificationService notificationService) {
        if (character.getClan() == null) {
            return 0;
        }

        // find relevant building, update notification and compute bonus
        for (Building building : character.getClan().getBuildings()) {
            if (building.getDetails().getBonusType() != null && building.getDetails().getBonusType().equals(bonusType)
                    && building.getLevel() > 0) {
                if (building.getDetails().getBonusText() != null) {
                    if (notification != null) {
                        NotificationDetail detail = new NotificationDetail();
                        notificationService.addLocalizedTexts(detail.getText(), building.getDetails().getBonusText(),
                                new String[] {character.getName()}, building.getDetails().getName());
                        notification.getDetails().add(detail);
                    }
                }
                return building.getDetails().getBonus() * building.getLevel();
            }
        }

        return 0;
    }

}
