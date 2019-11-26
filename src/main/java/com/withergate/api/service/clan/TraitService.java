package com.withergate.api.service.clan;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Trait service.
 *
 * @author Martin Myslik
 */
public interface TraitService {

    /**
     * Assigns all traits to provided character in inactive mode.
     *
     * @param character character
     */
    void assignTraits(Character character);

    /**
     * Activates the provided trait for given character. Throws an exception if conditions are not met.
     *
     * @param characterId character ID
     * @param clanId clan ID
     * @param traitName trait to be activated
     */
    void activateTrait(int characterId, int clanId, TraitName traitName) throws InvalidActionException;
}
