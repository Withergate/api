package com.withergate.api.service.item;

import java.util.List;

import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.request.BuildingRequest;
import com.withergate.api.game.model.request.CraftingRequest;
import com.withergate.api.service.exception.InvalidActionException;

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
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveCraftingAction(CraftingRequest request, int clanId) throws InvalidActionException;

    /**
     * Processes all crafting actions.
     *
     * @param turnId turn ID
     */
    void processCraftingActions(int turnId);

}
