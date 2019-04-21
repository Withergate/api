package com.withergate.api.service.item;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.EffectType;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.GearDetails;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.item.ConsumableDetailsRepository;
import com.withergate.api.repository.item.ConsumableRepository;
import com.withergate.api.repository.item.GearDetailsRepository;
import com.withergate.api.repository.item.GearRepository;
import com.withergate.api.repository.item.WeaponDetailsRepository;
import com.withergate.api.repository.item.WeaponRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final GearRepository gearRepository;
    private final GearDetailsRepository gearDetailsRepository;
    private final RandomService randomService;
    private final GameProperties gameProperties;
    private final NotificationService notificationService;

    public ItemServiceImpl(CharacterRepository characterRepository, ClanRepository clanRepository,
                           WeaponRepository weaponRepository, WeaponDetailsRepository weaponDetailsRepository,
                           ConsumableRepository consumableRepository,
                           ConsumableDetailsRepository consumableDetailsRepository,
                           GearRepository gearRepository,
                           GearDetailsRepository gearDetailsRepository,
                           RandomService randomService, GameProperties gameProperties,
                           NotificationService notificationService) {
        this.characterRepository = characterRepository;
        this.clanRepository = clanRepository;
        this.weaponRepository = weaponRepository;
        this.weaponDetailsRepository = weaponDetailsRepository;
        this.consumableRepository = consumableRepository;
        this.consumableDetailsRepository = consumableDetailsRepository;
        this.gearRepository = gearRepository;
        this.gearDetailsRepository = gearDetailsRepository;
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
    public void equipGear(int gearId, int characterId, int clanId) throws InvalidActionException {
        log.debug("Processing equip request with gear {} and character {}", gearId, characterId);

        /*
         * Load the gear.
         */
        Gear gear = gearRepository.getOne(gearId);

        if (gear == null) {
            log.error("Gear with ID {} not found!", gearId);
            throw new InvalidActionException("Gear with this ID was not found!");
        }

        Clan clan = gear.getClan();

        /*
         * Check if this weapon is present in the clan storage.
         */
        if (gear.getClan() == null || gear.getClan().getId() != clanId) {
            log.error("Gear with ID {} is not available in the clan storage of clan ID {}!", gearId, clanId);
            throw new InvalidActionException("Gear with this ID does not belong the the provided clan!");
        }

        /*
         * Check if the provided character exists and does not hold another gear already.
         */
        Character character = characterRepository.getOne(characterId);

        if (character == null || character.getGear() != null) {
            log.error("Character with ID {} is not able to equip the gear!", characterId);
            throw new InvalidActionException("The provided character either does not exist or already has a gear!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character must be READY to equip gear.");
        }

        /*
         * Equip the gear by attaching it to the character and removing it from the clan storage.
         */
        character.setGear(gear);
        characterRepository.save(character);

        clan.getGear().remove(gear);
        clanRepository.save(clan);

        gear.setClan(null);
        gear.setCharacter(character);
        gearRepository.save(gear);
    }

    @Transactional
    @Override
    public void unequipGear(int gearId, int characterId, int clanId) throws InvalidActionException {
        log.debug("Processing un-equip request with gear {} and character {}", gearId, characterId);

        /*
         * Load the gear.
         */
        Gear gear = gearRepository.getOne(gearId);

        if (gear == null) {
            log.error("Gear with ID {} not found!", gearId);
            throw new InvalidActionException("Gear with this ID was not found!");
        }

        Character character = gear.getCharacter();
        Clan clan = clanRepository.getOne(clanId);

        /*
         * Check if the gear and character belong to the provided clan.
         */
        if (character == null || character.getId() != characterId || character.getClan().getId() != clanId) {
            log.error("Character with ID {} does not match the requested character!", characterId);
            throw new InvalidActionException("Cannot equip weapon to the provided character!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character must be READY to un-equip weapon.");
        }

        /*
         * Check if the character already equips this gear.
         */
        if (character.getGear().getId() != gearId) {
            log.error("Character {} is not carrying the specified gear!", characterId);
            throw new InvalidActionException("Gear with this ID is not equipped by the provided character!");
        }

        character.setGear(null);
        characterRepository.save(character);

        clan.getGear().add(gear);
        clanRepository.save(clan);

        gear.setCharacter(null);
        gear.setClan(clan);
        gearRepository.save(gear);
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
        int diceRoll = randomService.getRandomInt(1, 3);
        switch (diceRoll) {
            case 1:
                generateWeapon(character, notification, getRandomRarity());
                break;
            case 2:
                generateConsumable(character, notification, getRandomRarity());
                break;
            case 3:
                generateGear(character, notification, getRandomRarity());
                break;
        }
    }

    @Override
    public void generateCraftableWeapon(Character character, int buildingLevel, ClanNotification notification) {
        log.debug("Crafting weapon with {}", character.getName());

        if (buildingLevel < 1) {
            return;
        }

        ItemDetails.Rarity rarity = getRandomRarity(character.getCraftsmanship(), buildingLevel);
        List<WeaponDetails> detailsList = weaponDetailsRepository.findAllByRarityAndCraftable(rarity, true);
        WeaponDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));

        Weapon weapon = new Weapon();
        weapon.setDetails(details);
        weapon.setClan(character.getClan());
        weaponRepository.save(weapon);

        NotificationDetail detail = new NotificationDetail();
        notificationService
                .addLocalizedTexts(detail.getText(), "detail.character.crafting", new String[]{character.getName()},
                        details.getName());
        notification.getDetails().add(detail);

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
         * Check prerequizities
         */
        if (consumable.getDetails().getEffectType().equals(EffectType.EXPERIENCE)
                && character.getIntellect() < consumable.getDetails().getPrereq()) {
            throw new InvalidActionException("Character's intellect is too low to perform this action.");
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
                break;
            case EXPERIENCE:
                character.setExperience(character.getExperience() + consumable.getDetails().getEffect());
                break;
            default:
                log.error("Unknown consumable type: {}!", consumable.getDetails().getEffectType());
        }

        // delete consumable
        consumableRepository.delete(consumable);

        // update
        clanRepository.save(clan);

    }

    private void generateWeapon(Character character, ClanNotification notification, ItemDetails.Rarity rarity) {
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
         * Place item it in the clan storage.
         */
        Clan clan = character.getClan();
        clan.getWeapons().add(weapon);
        weapon.setClan(clan);

        clanRepository.save(clan);
        weaponRepository.save(weapon);

        // update notification
        NotificationDetail detail = new NotificationDetail();
        notificationService
                .addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[]{character.getName()},
                        details.getName());
        notification.getDetails().add(detail);
    }

    private void generateConsumable(Character character, ClanNotification notification, ItemDetails.Rarity rarity) {
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
        notificationService
                .addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[]{character.getName()},
                        details.getName());
        notification.getDetails().add(detail);
    }

    private void generateGear(Character character, ClanNotification notification, ItemDetails.Rarity rarity) {
        /*
         * Load random weapon details .
         */
        List<GearDetails> gearDetailsList = gearDetailsRepository.findAllByRarity(rarity);
        GearDetails details = gearDetailsList.get(randomService.getRandomInt(0, gearDetailsList.size() - 1));

        /*
         * Create gear.
         */
        Gear gear = new Gear();
        gear.setDetails(details);

        /*
         * Place item it in the clan storage.
         */
        Clan clan = character.getClan();
        clan.getGear().add(gear);
        gear.setClan(clan);

        clanRepository.save(clan);
        gearRepository.save(gear);

        // update notification
        NotificationDetail detail = new NotificationDetail();
        notificationService
                .addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[]{character.getName()},
                        details.getName());
        notification.getDetails().add(detail);
    }

    private ItemDetails.Rarity getRandomRarity() {
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (diceRoll < gameProperties.getRareItemChance()) {
            return ItemDetails.Rarity.RARE;
        } else {
            return ItemDetails.Rarity.COMMON;
        }
    }

    private ItemDetails.Rarity getRandomRarity(int craftsmanship, int buildingLevel) {
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (diceRoll < (craftsmanship * 5 + buildingLevel * 5)) {
            return ItemDetails.Rarity.RARE;
        } else {
            return ItemDetails.Rarity.COMMON;
        }
    }
}
