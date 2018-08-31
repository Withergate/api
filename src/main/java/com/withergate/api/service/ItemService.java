package com.withergate.api.service;

import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.repository.CharacterRepository;
import com.withergate.api.repository.ClanRepository;
import com.withergate.api.repository.WeaponDetailsRepository;
import com.withergate.api.repository.WeaponRepository;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Item service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class ItemService implements IItemService {

    private final CharacterRepository characterRepository;
    private final ClanRepository clanRepository;
    private final WeaponRepository weaponRepository;
    private final WeaponDetailsRepository weaponDetailsRepository;
    private final RandomService randomService;

    /**
     * Constructor.
     *
     * @param characterRepository character repository
     * @param clanRepository      clan repository
     * @param weaponRepository    weapon repository
     * @param weaponDetailsRepository  weaponDetails repository
     * @param randomService random service
     */
    public ItemService(CharacterRepository characterRepository, ClanRepository clanRepository,
                       WeaponRepository weaponRepository, WeaponDetailsRepository weaponDetailsRepository,
                       RandomService randomService) {
        this.characterRepository = characterRepository;
        this.clanRepository = clanRepository;
        this.weaponRepository = weaponRepository;
        this.weaponDetailsRepository = weaponDetailsRepository;
        this.randomService = randomService;
    }

    @Transactional
    @Override
    public void equipWeapon(int weaponId, int characterId, int clanId) throws InvalidActionException {
        log.debug("Processing equip request with weapon {} and character {}", weaponId, characterId);

        /*
         * Load the weapon.
         */
        Weapon weapon = weaponRepository.getOne(weaponId);

        if (weapon == null) {
            log.error("Weapon with ID {} not found!", weaponId);
            throw new InvalidActionException("Weapon with this ID was not found!");
        }

        Clan clan = weapon.getClan();

        /*
         * Check if this weapon is present in the clan storage.
         */
        if (weapon.getClan() == null || weapon.getClan().getId() != clanId) {
            log.error("Weapon with ID {} is not available in the clan storage of clan ID {}!", weaponId, clanId);
            throw new InvalidActionException("Weapon with this ID does not belong the the provided clan!");
        }

        /*
         * Check if the provided character exists and does not hold another weapon already.
         */
        Character character = characterRepository.getOne(characterId);

        if (character == null || character.getWeapon() != null) {
            log.error("Character with ID {} is already holding a weapon!", characterId);
            throw new InvalidActionException("The provided character either does not exist or already has a weapon!");
        }

        /*
         * Equip the weapon by attaching it to the character and removing it from the clan storage.
         */
        character.setWeapon(weapon);
        characterRepository.save(character);

        clan.getWeapons().remove(weapon);
        clanRepository.save(clan);

        weapon.setClan(null);
        weapon.setCharacter(character);
        weaponRepository.save(weapon);
    }

    @Transactional
    @Override
    public void unequipWeapon(int weaponId, int characterId, int clanId) throws InvalidActionException {
        log.debug("Processing un-equip request with weapon {} and character {}", weaponId, characterId);

        /*
         * Load the weapon.
         */
        Weapon weapon = weaponRepository.getOne(weaponId);

        if (weapon == null) {
            log.error("Weapon with ID {} not found!", weaponId);
            throw new InvalidActionException("Weapon with this ID was not found!");
        }

        Character character = weapon.getCharacter();
        Clan clan = clanRepository.getOne(clanId);
        /*
         * Check if the weapon and character belong to the provided clan.
         *
         */
        if (character == null || character.getId() != characterId || character.getClan().getId() != clanId) {
            log.error("Character with ID {} does not match the requested character!", character.getId());
            throw new InvalidActionException("Cannot equip weapon to the provided character!");
        }

        /*
         * Check if the character is currently holding the weapon.
         */
        if (character.getWeapon().getId() != weaponId) {
            log.error("Character {} is not holding the specified weapon!", characterId);
            throw new InvalidActionException("Weapon with this ID is not equipped by the provided character!");
        }

        character.setWeapon(null);
        characterRepository.save(character);

        clan.getWeapons().add(weapon);
        clanRepository.save(clan);

        weapon.setCharacter(null);
        weapon.setClan(clan);
        weaponRepository.save(weapon);
    }

    @Transactional
    @Override
    public void generateItemForCharacter(Character character, StringBuilder notification) {
        log.debug("Generating random item for character {}", character.getId());

        /*
         * Load random weapon details .
         */
        List<WeaponDetails> weaponDetailsList = weaponDetailsRepository.findAll();
        WeaponDetails details = weaponDetailsList.get(randomService.getRandomInt(0, weaponDetailsList.size() - 1));

        /*
         * Create weapon.
         */
        Weapon weapon = new Weapon();
        weapon.setWeaponDetails(details);

        /*
         * Either equip the weapon with the character or place it in the clan storage.
         */
        notification.append("\n");
        notification.append("<b>" + details.getName() + "</b> was found!");

        if (character.getWeapon() == null) {
            character.setWeapon(weapon);
            weapon.setCharacter(character);

            characterRepository.save(character);
            weaponRepository.save(weapon);

            // update notification
            notification.append("\n");
            notification.append("<b>" + character.getName() + "</b> equipped this weapon.");
        } else {
            Clan clan = character.getClan();
            clan.getWeapons().add(weapon);
            weapon.setClan(clan);

            clanRepository.save(clan);
            weaponRepository.save(weapon);

            // update notification
            notification.append("\n");
            notification.append("<b>" + character.getName() + "</b> took this item to your clan storage.");
        }
    }
}
