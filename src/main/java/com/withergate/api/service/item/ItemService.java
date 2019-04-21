package com.withergate.api.service.item;

import com.withergate.api.model.character.Character;
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
     * @param weaponId    the weapon ID
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException
     */
    void equipWeapon(int weaponId, int characterId, int clanId) throws InvalidActionException;

    /**
     * Un-equip a weapon from the specified character. Throws an exception if this action is not allowed.
     *
     * @param weaponId    the weapon ID
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException
     */
    void unequipWeapon(int weaponId, int characterId, int clanId) throws InvalidActionException;

    /**
     * Equip a gear with the specified character. Throws an exception if this action is not allowed.
     *
     * @param gearId    the gear ID
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException
     */
    void equipGear(int gearId, int characterId, int clanId) throws InvalidActionException;

    /**
     * Un-equip a gear from the specified character. Throws an exception if this action is not allowed.
     *
     * @param gearId    the gear ID
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException
     */
    void unequipGear(int gearId, int characterId, int clanId) throws InvalidActionException;

    /**
     * Generate a random item for the provided character.
     *
     * @param character    the character
     * @param notification the notification to be updated
     */
    void generateItemForCharacter(Character character, ClanNotification notification);

    /**
     * Generates craftable weapon.
     *
     * @param character     the character performing the crafting
     * @param buildingLevel the construction building level
     * @param notification  the crafted weapon
     */
    void generateCraftableWeapon(Character character, int buildingLevel, ClanNotification notification);

    /**
     * Use the consumable with the provided character.
     *
     * @param consumableId the consumable ID
     * @param characterId  the character ID
     * @param clanId       the clan ID
     * @throws InvalidActionException
     */
    void useConsumable(int consumableId, int characterId, int clanId) throws InvalidActionException;

}
