package com.withergate.api.service.clan;

import java.util.List;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Trait;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Character service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class CharacterServiceImpl implements CharacterService {

    public static final int LEVEL_QUOTIENT = 10;
    public static final int FREE_TRAIT_THRESHOLD = 10;

    private final CharacterRepository characterRepository;
    private final RandomService randomService;
    private final NameService nameService;
    private final TraitService traitService;

    @Override
    public Character load(int characterId) {
        return characterRepository.getOne(characterId);
    }

    @Override
    public List<Character> loadAll() {
        return characterRepository.findAll();
    }

    @Override
    public Character save(Character character) {
        return characterRepository.save(character);
    }

    @Override
    public void delete(Character character) {
        characterRepository.delete(character);
    }

    @Override
    public Character generateRandomCharacter(CharacterFilter filter) {
        Character character = new Character();

        // set character's state to READY
        character.setState(CharacterState.READY);

        // generate random gender
        Gender gender = randomService.getRandomGender();
        character.setGender(gender);

        // generate random name
        String name = nameService.generateRandomName(gender, filter.getNames());
        character.setName(name);

        // generate random hitpoints
        int hitpoints = RandomServiceImpl.K10 + randomService.getRandomInt(1, RandomServiceImpl.K10);
        character.setHitpoints(hitpoints);
        character.setMaxHitpoints(hitpoints);

        // generate random stats
        character.setCombat(getRandomAbilityValue());
        character.setScavenge(getRandomAbilityValue());
        character.setCraftsmanship(getRandomAbilityValue());
        character.setIntellect(getRandomAbilityValue());

        // generate random avatar
        character.setImageUrl(nameService.generateRandomAvatar(gender, filter.getAvatars()));

        // set level
        character.setLevel(1);
        character.setExperience(0);

        // add random trait to weak veterans
        if ((character.getCombat() + character.getScavenge()
                + character.getCraftsmanship() + character.getIntellect()) <= FREE_TRAIT_THRESHOLD) {
            Trait trait = traitService.getRandomTrait(character);
            character.getTraits().put(trait.getDetails().getIdentifier(), trait);
        }

        return character;
    }

    @Override
    public void markCharacterAsResting(int characterId, int clanId) throws InvalidActionException {
        // check action validity
        Character character = load(characterId);
        if (character == null || character.getClan().getId() != clanId) {
            throw new InvalidActionException("This character either doesn't exist or doesn't belong to your clan.");
        }

        if (!character.getState().equals(CharacterState.READY)) {
            throw new InvalidActionException("Character must be ready to perform this action.");
        }

        character.setState(CharacterState.RESTING);
        save(character);
    }

    private int getRandomAbilityValue() {
        // random ability value is the average between two k6 dice rolls rounded down to ensure fair distribution
        int value1 = randomService.getRandomInt(1, RandomServiceImpl.K6);
        int value2 = randomService.getRandomInt(1, RandomServiceImpl.K6);

        return (int) ((value1 + value2) / 2.0);
    }

}
