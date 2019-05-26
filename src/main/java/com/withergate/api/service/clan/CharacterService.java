package com.withergate.api.service.clan;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.service.exception.InvalidActionException;

import java.util.List;

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
     * Loads all characters from database.
     *
     * @return the list of loaded characters
     */
    List<Character> loadAll();

    /**
     * Saves the provided character.
     * 
     * @param character the character to be saved
     * @return the saved character
     */
    Character save(Character character);

    /**
     * Deletes the provided character.
     *
     * @param character the character to be deleted
     */
    void delete(Character character);

    /**
     * Updates all characters. Meant to be called at the end of each turn.
     */
    void performCharacterTurnUpdates(int turnId);

    /**
     * Generates random character.
     *
     * @param filter filter
     * @return the generated character
     */
    Character generateRandomCharacter(CharacterFilter filter);

    /**
     * Marks character as resting.
     *
     * @param characterId the character ID
     * @param clanId      the clan ID
     * @throws InvalidActionException invalid action
     */
    void markCharacterAsResting(int characterId, int clanId) throws InvalidActionException;
}
