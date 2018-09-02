package com.withergate.api.service;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.repository.CharacterRepository;
import com.withergate.api.repository.ClanNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Character service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class CharacterService implements ICharacterService {

    private final CharacterRepository characterRepository;
    private final RandomService randomService;
    private final ClanNotificationRepository clanNotificationRepository;
    private final NameService nameService;

    /**
     * Constructor.
     *
     * @param characterRepository        character repository
     * @param randomService              random service
     * @param clanNotificationRepository playerNotification repository
     * @param nameService                name service
     */
    public CharacterService(CharacterRepository characterRepository, RandomService randomService,
                            ClanNotificationRepository clanNotificationRepository, NameService nameService) {
        this.characterRepository = characterRepository;
        this.randomService = randomService;
        this.clanNotificationRepository = clanNotificationRepository;
        this.nameService = nameService;
    }

    @Override
    public Character load(int characterId) {
        return characterRepository.getOne(characterId);
    }

    @Override
    public Character save(Character character) {
        return characterRepository.save(character);
    }

    @Transactional
    @Override
    public void performCharacterHealing(int turnId) {
        log.debug("Performing healing action");

        // load all injured characters
        List<Character> characters = characterRepository.findAllByState(CharacterState.INJURED);

        for (Character character : characters) {
            // prepare notification
            ClanNotification notification = new ClanNotification();
            notification.setTurnId(turnId);
            notification.setClanId(character.getClan().getId());

            // each character has a chance to be healed this turn
            int diceRoll = randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE);
            if (diceRoll < 50) { // success
                character.setState(CharacterState.READY);
                characterRepository.save(character);

                notification.setText("[" + character.getName() + "] has recovered as is ready again.");
            } else { // failure
                notification.setText("[" + character.getName() + "] did not manage to recover yet.");
            }
            notification.setDetails("Rolled " + diceRoll + " when computing healing probability.");
            clanNotificationRepository.save(notification);
        }

    }

    @Override
    public Character generateRandomCharacter() {
        Character character = new Character();

        /*
         * Set character's state to READY.
         */
        character.setState(CharacterState.READY);

        /*
         * Generate random gender.
         */
        Gender gender = randomService.getRandomGender();
        character.setGender(gender);

        /*
         * Generate random name.
         */
        String name = nameService.generateRandomName(gender);
        character.setName(name);

        /*
         * Generate random stats.
         */
        character.setCombat(randomService.getRandomInt(1, 5));
        character.setScavenge(randomService.getRandomInt(1, 5));
        character.setCraftsmanship(randomService.getRandomInt(1, 5));
        character.setIntellect(randomService.getRandomInt(1, 5));
        character.setCharm(randomService.getRandomInt(1, 5));

        return character;
    }
}
