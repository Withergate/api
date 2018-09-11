package com.withergate.api.service.clan;

import com.withergate.api.model.character.Character;

/**
 * CharacterService interface.
 *
 * @author Martin Myslik
 */
public interface ICharacterService {

    Character load(int characterId);

    Character save(Character character);

    void delete(Character character);

    void performCharacterHealing(int turnId);

    Character generateRandomCharacter();
}
