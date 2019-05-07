package com.withergate.api.service.item;

import com.withergate.api.model.character.Character;
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
     * Equip a weapon with the specified character. Throws an exception if this action is not allowed.
     *
     * @param itemId      the weapon ID
     * @param type        item type
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException invalid action
     */
    void equipItem(int itemId, ItemType type, int characterId, int clanId) throws InvalidActionException;

    /**
     * Un-equip a weapon from the specified character. Throws an exception if this action is not allowed.
     *
     * @param itemId      the weapon ID
     * @param type        item type
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException invalid action
     */
    void unequipItem(int itemId, ItemType type, int characterId, int clanId) throws InvalidActionException;

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
     * @param notification  the crafted weapon
     * @param type          the item type
     */
    void generateCraftableItem(Character character, int buildingLevel, ClanNotification notification, ItemType type);

    /**
     * Use the consumable with the provided character.
     *
     * @param consumableId the consumable ID
     * @param characterId  the character ID
     * @param clanId       the clan ID
     * @throws InvalidActionException invalid action
     */
    void useConsumable(int consumableId, int characterId, int clanId) throws InvalidActionException;

}
