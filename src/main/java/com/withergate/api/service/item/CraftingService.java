package com.withergate.api.service.item;

import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.request.CraftingRequest;
import com.withergate.api.service.action.Actionable;

import java.util.List;

/**
 * Crafting service.
 *
 * @author  Martin Myslik
 */
public interface CraftingService extends Actionable<CraftingRequest> {

    /**
     * Loads all available items to craft for the provided clan.
     *
     * @param clanId clan ID
     * @return list of item details
     */
    List<ItemDetails> getAvailableItems(int clanId);

}
