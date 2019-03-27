package com.withergate.api.service.clan;

import com.withergate.api.model.character.Character;

/**
 * CharacterService interface.
 *
 * @author Martin Myslik
 */
public interface CharacterService {

    /**
     * Loads single character from database.
     * 
     * @param characterId character ID
     * @return the character
     */
    Character load(int characterId);

    /**
     * Saves the provided character.
     * 
     * @param character the character to be saved
     * @return the saved character
     */
    Character save(Character character);

    /**
     * Deletes all characters with less than 1 health remaining.
     */
    void deleteDeadCharacters();

    /**
     * Updates all characters. Meant to be called at the end of each turn.
     */
    void performCharacterTurnUpdates(int turnId);

    /**
     * Generates random character.
     *
     * @return the generated character
     */
    Character generateRandomCharacter();
}
