package com.withergate.api.service.clan;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
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

    @Override
    public void delete(Character character) {
        characterRepository.delete(character);
    }

    @Transactional
    @Override
    public void performCharacterHealing(int turnId) {
        log.debug("Performing healing action");

        // load all characters that are ready
        List<Character> characters = characterRepository.findAllByState(CharacterState.READY);

        for (Character character : characters) {
            int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();

            if (hitpointsMissing == 0) {
                continue;
            }

            // prepare notification
            ClanNotification notification = new ClanNotification();
            notification.setTurnId(turnId);
            notification.setClanId(character.getClan().getId());

            // each character that is ready heals
            int diceRoll = randomService.getRandomInt(1, 2);


            int healing = Math.min(diceRoll, hitpointsMissing);
            character.setHitpoints(character.getHitpoints() + healing);

            characterRepository.save(character);

            notification.setText("[" + character.getName() + "] has healed " + diceRoll + " hitpoints.");
            notification.setHealing(diceRoll);

            notification.setDetails("Rolled " + diceRoll + " when computing healing.");
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
         * Generate random hitpoints.
         */
        int hitpoints = randomService.getRandomInt(1, 5) + randomService.getRandomInt(1, 5);
        character.setHitpoints(hitpoints);
        character.setMaxHitpoints(hitpoints);

        /*
         * Generate random stats.
         */
        character.setCombat(randomService.getRandomInt(1, 5));
        character.setScavenge(randomService.getRandomInt(1, 5));
        character.setCraftsmanship(randomService.getRandomInt(1, 5));
        character.setIntellect(randomService.getRandomInt(1, 5));

        /*
         * Generate random avatar.
         */
        character.setImageUrl(nameService.generateRandomAvatar(gender));

        return character;
    }
}
