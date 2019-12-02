package com.withergate.api.service.clan;

import java.util.List;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
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
    private final NotificationService notificationService;

    @Override
    public Character load(int characterId) {
        return characterRepository.getOne(characterId);
    }

    @Override
    public Character loadReadyCharacter(int characterId, int clanId) throws InvalidActionException {
        Character character = characterRepository.getOne(characterId);
        if (character.getClan().getId() != clanId || character.getState() != CharacterState.READY) {
            log.error("Action cannot be performed with this character: {}!", character.getId());
            throw new InvalidActionException("The provided character is not ready or does not belong to your clan!");
        }

        return character;
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
    public Character generateRandomCharacter(CharacterFilter filter, int[] attributes) {
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
        character.setCombat(attributes[0]);
        character.setScavenge(attributes[1]);
        character.setCraftsmanship(attributes[2]);
        character.setIntellect(attributes[3]);

        // generate random avatar
        character.setImageUrl(nameService.generateRandomAvatar(gender, filter.getAvatars()));

        // set level
        character.setLevel(1);
        character.setExperience(0);

        // init traits
        traitService.assignTraits(character);

        // add skill point to weak characters
        if ((character.getCombat() + character.getScavenge()
                + character.getCraftsmanship() + character.getIntellect()) <= FREE_TRAIT_THRESHOLD) {
            character.setSkillPoints(1);
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

    @Override
    public void increaseCharacterLevel(Character character, int turnId) {
        log.debug("Character {} leveled up.", character.getName());

        character.changeExperience(- character.getNextLevelExperience());
        int hpIncrease = randomService.getRandomInt(1, RandomServiceImpl.K6);
        character.setMaxHitpoints(character.getMaxHitpoints() + hpIncrease);
        character.changeHitpoints(hpIncrease);
        character.setLevel(character.getLevel() + 1);

        // notification
        ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
        notification.setHeader(character.getName());
        notification.setImageUrl(character.getImageUrl());
        notificationService.addLocalizedTexts(notification.getText(), "character.levelup", new String[] {character.getName()});

        // add skill points character
        character.setSkillPoints(character.getSkillPoints() + 1);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.character.levelup.trait",
                new String[] {character.getName()});
        notification.getDetails().add(detail);

        // save
        notificationService.save(notification);
    }

}
