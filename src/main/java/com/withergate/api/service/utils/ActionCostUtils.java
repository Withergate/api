package com.withergate.api.service.utils;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.cost.ActionCost;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.item.ItemService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ActionCostUtils {

    /**
     * Handles cost payment during action evaluation.
     *
     * @param cost cost
     * @param character character
     * @param notification notification
     * @param randomService random service
     * @param itemService item service
     */
    public static void handlePostActionPayment(ActionCost cost, Character character, ClanNotification notification,
                                              RandomService randomService, ItemService itemService) {
        if (cost.isHealthCost()) {
            int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);
            character.changeHitpoints(-injury);
            notification.changeInjury(injury);
            if (character.getHitpoints() < 1) notification.setDeath(true);
        }

        if (cost.getItemCost() != null) {
            itemService.deleteItem(character, cost.getItemCost(), notification);
        }
    }
}
