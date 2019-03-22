package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.notification.NotificationDetail;
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

    public static final int LEVEL_QUOCIENT = 10;

    private final CharacterRepository characterRepository;
    private final RandomService randomService;
    private final ClanNotificationRepository clanNotificationRepository;
    private final NameService nameService;

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
    public void performCharacterTurnUpdates(int turnId) {
        for (Character character : characterRepository.findAll()) {
            if (character.getHitpoints() < 1) {
                log.debug("Deleting dead character: {}", character.getName());
                characterRepository.delete(character);
            } else {
                character.setState(CharacterState.READY);
                characterRepository.save(character);
            }
        }

        // eat food
        eatFood(turnId);

        // perform character healing
        performCharacterHealing(turnId);

        // perform character leveling
        performCharacterLeveling(turnId);
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
        int hitpoints = RandomService.K10 + randomService.getRandomInt(1, RandomService.K10);
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

        /*
         * Set level.
         */
        character.setLevel(1);
        character.setExperience(0);

        return character;
    }

    private void eatFood(int turnId) {
        log.debug("Eating food.");

        for (Character character : characterRepository.findAll()) {
            Clan clan = character.getClan();
            if (clan.getFood() > 0) {
                clan.setFood(clan.getFood() - 1);
            } else {
                log.debug("Character {} is starving,", character.getName());

                character.setHitpoints(character.getHitpoints() - 1);

                ClanNotification notification = new ClanNotification();
                notification.setClanId(clan.getId());
                notification.setTurnId(turnId);
                notification.setText("[" + character.getName() + "] is starving.");
                notification.setInjury(1);

                if (character.getHitpoints() < 1) {
                    log.debug("Character {} died of starvation.", character.getName());

                    NotificationDetail detail = new NotificationDetail();
                    detail.setText("[" + character.getName() + "] died of starvation.");
                    notification.getDetails().add(detail);

                    characterRepository.delete(character);
                } else {
                    characterRepository.save(character);
                }

                clanNotificationRepository.save(notification);

            }
        }
    }

    private void performCharacterHealing(int turnId) {
        log.debug("Performing healing action");

        // load all characters that are ready
        List<Character> characters = characterRepository.findAllByState(CharacterState.READY);

        for (Character character : characters) {
            // skip if there is no food
            if (character.getClan().getFood() < 1) continue;

            int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();

            if (hitpointsMissing == 0) {
                continue;
            }

            // prepare notification
            ClanNotification notification = new ClanNotification();
            notification.setTurnId(turnId);
            notification.setClanId(character.getClan().getId());

            // each character that is ready heals
            int points = randomService.getRandomInt(1, 2);
            NotificationDetail detail1 = new NotificationDetail();
            detail1.setText("Rolled " + points + " when computing healing.");
            notification.getDetails().add(detail1);
            if (character.getClan().getBuildings().containsKey(BuildingDetails.BuildingName.SICK_BAY)) {
                Building building = character.getClan().getBuildings().get(BuildingDetails.BuildingName.SICK_BAY);
                points += building.getLevel();

                if (building.getLevel() > 0) {
                    NotificationDetail detail2 = new NotificationDetail();
                    detail2.setText("Healing increased by " + building.getLevel() +" by having sick bay.");
                    notification.getDetails().add(detail2);
                }
            }

            int healing = Math.min(points, hitpointsMissing);
            character.setHitpoints(character.getHitpoints() + healing);

            characterRepository.save(character);

            notification.setText("[" + character.getName() + "] has healed " + points + " hitpoints.");
            notification.setHealing(points);


            clanNotificationRepository.save(notification);
        }
    }

    private void performCharacterLeveling(int turnId) {
        log.debug("Performing character leveling action");

        // load all characters
        List<Character> characters = characterRepository.findAll();

        for (Character character : characters) {
            if (character.getExperience() >= character.getNextLevelExperience()) {
                // level up
                log.debug("Character {} leveled up.", character.getName());

                character.setExperience(character.getExperience() - character.getNextLevelExperience());
                int hpIncrease = randomService.getRandomInt(1, RandomService.K6);
                character.setMaxHitpoints(character.getMaxHitpoints() + hpIncrease);
                character.setHitpoints(character.getHitpoints() + hpIncrease);
                character.setLevel(character.getLevel() + 1);
                characterRepository.save(character);

                // send notification
                ClanNotification notification = new ClanNotification();
                notification.setTurnId(turnId);
                notification.setClanId(character.getClan().getId());
                notification.setText("[" + character.getName() +"] leveled up and is now on level " + character.getLevel() + ".");
                clanNotificationRepository.save(notification);
            }
        }
    }

}
