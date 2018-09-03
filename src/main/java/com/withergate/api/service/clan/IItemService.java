package com.withergate.api.service.clan;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.character.Character;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * ItemService interface.
 *
 * @author Martin Myslik
 */
public interface IItemService {

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
     * Generate a random item for the provided character.
     *
     * @param character the character
     * @param notification the notification to be updated
     */
    void generateItemForCharacter(Character character, ClanNotification notification);

}
