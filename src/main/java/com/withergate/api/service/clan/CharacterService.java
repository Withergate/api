package com.withergate.api.service.clan;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterFilter;
import com.withergate.api.game.model.request.DefaultActionRequest;
import com.withergate.api.game.model.type.AttributeTemplate;
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
     * Loads character in READY state and checks if he belongs to the provided clan. Throws an exception otherwise.
     *
     * @param characterId character ID
     * @param clanId clan ID
     * @return loaded character
     */
    Character loadReadyCharacter(int characterId, int clanId) throws InvalidActionException;

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
     * Deletes all dead characters.
     */
    void deleteDeadCharacters();

    /**
     * Generates random character.
     *
     * @param filter filter
     * @return the generated character
     */
    Character generateRandomCharacter(CharacterFilter filter, AttributeTemplate attributes);

    /**
     * Increases level for provided character.
     *
     * @param character character
     * @param turnId turn
     */
    void increaseCharacterLevel(Character character, int turnId);

    /**
     * Cancels current action of the character if the action is cancellable.
     *
     * @param characterId character ID
     * @param clanId clan ID
     * @throws InvalidActionException invalid action
     */
    void cancelAction(int characterId, int clanId) throws InvalidActionException;

    /**
     * Renames provided character. Throws an exception if action is not valid. Premium feature.
     *
     * @param characterId character ID
     * @param clanId clan ID
     * @param name new name
     * @throws InvalidActionException invalid action
     */
    void renameCharacter(int characterId, int clanId, String name) throws InvalidActionException;

    /**
     * Changes character's default action.
     *
     * @param request the request
     * @param clanId  the clan ID
     */
    void changeDefaultAction(DefaultActionRequest request, int clanId, int characterId) throws InvalidActionException;

}
