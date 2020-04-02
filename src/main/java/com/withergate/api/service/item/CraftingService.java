package com.withergate.api.service.item;

import java.util.List;

import com.withergate.api.game.model.item.ItemDetails;

/**
 * Crafting service.
 *
 * @author  Martin Myslik
 */
public interface CraftingService {

    /**
     * Loads all available items to craft for the provided clan.
     *
     * @param clanId clan ID
     * @return list of item details
     */
    List<ItemDetails> getAvailableItems(int clanId);

    /**
     * Processes all crafting actions.
     *
     * @param turnId turn ID
     */
    void processCraftingActions(int turnId);

}
