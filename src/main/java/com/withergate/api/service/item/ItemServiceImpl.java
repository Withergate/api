package com.withergate.api.service.item;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.Rarity;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.item.ConsumableDetailsRepository;
import com.withergate.api.repository.item.ConsumableRepository;
import com.withergate.api.repository.item.WeaponDetailsRepository;
import com.withergate.api.repository.item.WeaponRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
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
public class ItemServiceImpl implements ItemService {

    private final CharacterRepository characterRepository;
    private final ClanRepository clanRepository;
    private final WeaponRepository weaponRepository;
    private final WeaponDetailsRepository weaponDetailsRepository;
    private final ConsumableRepository consumableRepository;
    private final ConsumableDetailsRepository consumableDetailsRepository;
    private final RandomService randomService;
    private final GameProperties gameProperties;
    private final NotificationService notificationService;

    public ItemServiceImpl(CharacterRepository characterRepository, ClanRepository clanRepository,
                           WeaponRepository weaponRepository, WeaponDetailsRepository weaponDetailsRepository,
                           ConsumableRepository consumableRepository, ConsumableDetailsRepository consumableDetailsRepository,
                           RandomService randomService, GameProperties gameProperties, NotificationService notificationService) {
        this.characterRepository = characterRepository;
        this.clanRepository = clanRepository;
        this.weaponRepository = weaponRepository;
        this.weaponDetailsRepository = weaponDetailsRepository;
        this.consumableRepository = consumableRepository;
        this.consumableDetailsRepository = consumableDetailsRepository;
        this.randomService = randomService;
        this.gameProperties = gameProperties;
        this.notificationService = notificationService;
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
            log.error("Character with ID {} is not able to equip the weapon!", characterId);
            throw new InvalidActionException("The provided character either does not exist or already has a weapon!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character must be READY to equip weapon.");
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
            log.error("Character with ID {} does not match the requested character!", characterId);
            throw new InvalidActionException("Cannot equip weapon to the provided character!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character must be READY to un-equip weapon.");
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
    public void generateItemForCharacter(Character character, ClanNotification notification) {
        log.debug("Generating random item for character {}", character.getId());

        /*
         * Get random item type
         */
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (diceRoll < 50) {
            generateWeapon(character, notification, getRandomRarity());
        } else {
            generateConsumable(character, notification, getRandomRarity());
        }
    }

    @Transactional
    @Override
    public void useConsumable(int consumableId, int characterId, int clanId) throws InvalidActionException {
        log.debug("Using consumable {} with character {}", consumableId, characterId);

        /*
         * Load the character.
         */
        Character character = characterRepository.getOne(characterId);
        if (character == null || character.getClan().getId() != clanId) {
            throw new InvalidActionException("Character not found or doesn't belong to your clan!");
        }

        /*
         * Load the consumable.
         */
        Consumable consumable = consumableRepository.getOne(consumableId);
        if (consumable == null || consumable.getClan().getId() != clanId) {
            throw new InvalidActionException("Consumable not found or doesn't belong to your clan!");
        }

        /*
         * Process the effect.
         */
        Clan clan = character.getClan();

        switch (consumable.getDetails().getEffectType()) {
            case HEALING:
                int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();
                if (hitpointsMissing == 0) {
                    throw new InvalidActionException("This character already has full health!");
                }
                int healing = Math.min(hitpointsMissing, consumable.getDetails().getEffect());
                character.setHitpoints(character.getHitpoints() + healing);
                log.debug("{} healed {} from consumable.", character.getName(), healing);

                // delete consumable
                consumableRepository.delete(consumable);

                // update
                clanRepository.save(clan);

                break;
            default:
                log.error("Unknown consumable type: {}!", consumable.getDetails().getEffectType());
        }

    }

    private void generateWeapon(Character character, ClanNotification notification, Rarity rarity) {
        log.debug("Generating random weapon!");

        /*
         * Load random weapon details .
         */
        List<WeaponDetails> weaponDetailsList = weaponDetailsRepository.findAllByRarity(rarity);
        WeaponDetails details = weaponDetailsList.get(randomService.getRandomInt(0, weaponDetailsList.size() - 1));

        /*
         * Create weapon.
         */
        Weapon weapon = new Weapon();
        weapon.setDetails(details);

        /*
         * Either equip the weapon with the character or place it in the clan storage.
         */
        if (character.getWeapon() == null) {
            character.setWeapon(weapon);
            weapon.setCharacter(character);

            characterRepository.save(character);
            weaponRepository.save(weapon);

            // update notification
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.item.found.equipped", new String[]{character.getName()});
            notification.getDetails().add(detail);
        } else {
            Clan clan = character.getClan();
            clan.getWeapons().add(weapon);
            weapon.setClan(clan);

            clanRepository.save(clan);
            weaponRepository.save(weapon);

            // update notification
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[]{character.getName()});
            notification.getDetails().add(detail);
        }
    }

    private void generateConsumable(Character character, ClanNotification notification, Rarity rarity) {
        log.debug("Generating random consumable.");

        /*
         * Load random weapon details .
         */
        List<ConsumableDetails> detailsList = consumableDetailsRepository.findAllByRarity(rarity);
        ConsumableDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));

        /*
         * Create consumable.
         */
        Consumable consumable = new Consumable();
        consumable.setDetails(details);

        /*
         * Add it to clan storage.
         */
        Clan clan = character.getClan();

        consumable.setClan(clan);
        clan.getConsumables().add(consumable);
        clanRepository.save(clan);

        /*
         * Update notification.
         */
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[]{character.getName()});
        notification.getDetails().add(detail);

    }

    private Rarity getRandomRarity() {
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (diceRoll < gameProperties.getRareItemChance()) {
            return Rarity.RARE;
        } else {
            return Rarity.COMMON;
        }
    }
}
