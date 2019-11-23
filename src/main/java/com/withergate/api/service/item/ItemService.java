package com.withergate.api.service.item;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * ItemService interface.
 *
 * @author Martin Myslik
 */
public interface ItemService {

    /**
     * Loads item by id.
     *
     * @param itemId item ID
     * @return the loaded item
     */
    Item loadItem(int itemId);

    /**
     * Equip a item with the specified character. Throws an exception if this action is not allowed.
     *
     * @param itemId      the weapon ID
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException invalid action
     */
    void equipItem(int itemId, int characterId, int clanId) throws InvalidActionException;

    /**
     * Un-equip a weapon from the specified character. Throws an exception if this action is not allowed.
     *
     * @param itemId      the weapon ID
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException invalid action
     */
    void unequipItem(int itemId, int characterId, int clanId) throws InvalidActionException;

    /**
     * Generate a random item for the provided character.
     *
     * @param character    the character
     * @param notification the notification to be updated
     */
    void generateItemForCharacter(Character character, ClanNotification notification);

    /**
     * Generates craftable item.
     *
     * @param character     the character performing the crafting
     * @param buildingLevel the construction building level
     * @param bonus         additional bonus to crafting
     * @param notification  the crafted weapon
     * @param type          the item type
     */
    void generateCraftableItem(Character character, int buildingLevel, int bonus, ClanNotification notification, ItemType type);

    /**
     * Deletes the provided item.
     *
     * @param item the item to be deleted
     */
    void deleteItem(Item item);

}
