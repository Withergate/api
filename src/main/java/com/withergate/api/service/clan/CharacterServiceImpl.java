package com.withergate.api.service.clan;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterFilter;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Gender;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.repository.action.BaseActionRepository;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.profile.model.PremiumType;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.premium.Premium;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private static final int MIN_NAME_LENGTH = 6;
    private static final int MAX_NAME_LENGTH = 30;

    private final CharacterRepository characterRepository;
    private final RandomService randomService;
    private final NameService nameService;
    private final TraitService traitService;
    private final NotificationService notificationService;
    private final BaseActionRepository actionRepository;

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

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void deleteDeadCharacters() {
        for (Character character : loadAll()) {
            if (character.getHitpoints() < 1) {
                delete(character);
            }
        }
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

    @Transactional
    @Premium(type = PremiumType.GOLD)
    @Override
    public void cancelAction(int characterId, int clanId) throws InvalidActionException {
        Character character = characterRepository.getOne(characterId);

        // check conditions
        if (character.getClan().getId() != clanId) {
            throw new InvalidActionException("Character must belong to your clan to perform this action.");
        }
        if (character.getCurrentAction().isEmpty() || !character.getCurrentAction().get().isCancellable()) {
            throw new InvalidActionException("Character has no action or this action is not cancellable.");
        }

        // cancel action
        actionRepository.delete(character.getCurrentAction().get());
        character.getActions().remove(character.getCurrentAction().get());
        character.setState(CharacterState.READY);
    }

    @Transactional
    @Premium(type = PremiumType.GOLD)
    @Override
    public void renameCharacter(int characterId, int clanId, String name) throws InvalidActionException {
        Character character = characterRepository.getOne(characterId);

        // check conditions
        if (character.getClan().getId() != clanId) {
            throw new InvalidActionException("Character must belong to your clan to perform this action.");
        }

        // sanitize
        name = name.replaceAll("\\s+", " ").trim();
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidActionException("Character name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH
                + " characters long.");
        }

        // capitalize
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        // check name collisions
        for (Character ch : character.getClan().getCharacters()) {
            if (ch.getName().equals(name)) {
                throw new InvalidActionException("Character with this name already exists in your clan!");
            }
        }

        // rename
        character.setName(name);
    }

    private void delete(Character character) {
        try {
            log.debug("Deleting character {}.", character.getId());
            if (character.getClan() != null) {
                character.getClan().getCharacters().remove(character);
            }
            character.setClan(null);
            character.setOffer(null);
            characterRepository.delete(character);
        } catch (Throwable e) {
            log.error("Cannot delete character.", e);
        }

    }

}
