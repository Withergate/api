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
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.OutfitDetails;
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
import com.withergate.api.repository.item.OutfitDetailsRepository;
import com.withergate.api.repository.item.OutfitRepository;
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
    private final OutfitRepository outfitRepository;
    private final OutfitDetailsRepository outfitDetailsRepository;
    private final RandomService randomService;
    private final GameProperties gameProperties;
    private final NotificationService notificationService;

    public ItemServiceImpl(CharacterRepository characterRepository, ClanRepository clanRepository,
                           WeaponRepository weaponRepository, WeaponDetailsRepository weaponDetailsRepository,
                           ConsumableRepository consumableRepository,
                           ConsumableDetailsRepository consumableDetailsRepository,
                           GearRepository gearRepository,
                           GearDetailsRepository gearDetailsRepository,
                           OutfitRepository outfitRepository,
                           OutfitDetailsRepository outfitDetailsRepository,
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
        this.outfitRepository = outfitRepository;
        this.outfitDetailsRepository = outfitDetailsRepository;
        this.randomService = randomService;
        this.gameProperties = gameProperties;
        this.notificationService = notificationService;
    }

    @Transactional
    @Override
    public void equipItem(int itemId, ItemType type, int characterId, int clanId) throws InvalidActionException {
        Item item = null;
        boolean equipped = false; // item already equipped

        Character character = characterRepository.getOne(characterId);

        if (character == null) {
            log.error("Character with ID {} not found!", characterId);
            throw new InvalidActionException("The provided character not found!");
        }

        // load item
        if (type.equals(ItemType.WEAPON)) {
            item = weaponRepository.getOne(itemId);
            if (character.getWeapon() != null) equipped = true;
        }
        if (type.equals(ItemType.GEAR)) {
            item = gearRepository.getOne(itemId);
            if (character.getGear() != null) equipped = true;
        }
        if (type.equals(ItemType.OUTFIT)) {
            item = outfitRepository.getOne(itemId);
            if (character.getOutfit() != null) equipped = true;
        }

        if (item == null) {
            log.error("Item with ID {} not found!", itemId);
            throw new InvalidActionException("Item with this ID was not found!");
        }

        if (equipped) {
            log.error("Character with ID {} is not able to equip the item!", characterId);
            throw new InvalidActionException("The provided character already has an item of the same type!");
        }

        Clan clan = item.getClan();

        // check clan
        if (item.getClan() == null || item.getClan().getId() != clanId) {
            log.error("Item with ID {} is not available in the clan storage of clan ID {}!", itemId, clanId);
            throw new InvalidActionException("Item with this ID does not belong the the provided clan!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character must be READY to equip item.");
        }

        // process equip actions
        if (type.equals(ItemType.WEAPON)) {
            equipWeapon((Weapon) item, character, clan);
        }
        if (type.equals(ItemType.GEAR)) {
            equipGear((Gear) item, character, clan);
        }
        if (type.equals(ItemType.OUTFIT)) {
            equipOutfit((Outfit) item, character, clan);
        }
    }

    @Transactional
    @Override
    public void unequipItem(int itemId, ItemType type, int characterId, int clanId) throws InvalidActionException {
        Item item = null;
        Character character = null;
        int equippedId = 0;

        // load item
        if (type.equals(ItemType.WEAPON)) {
            Weapon weapon = weaponRepository.getOne(itemId);
            item = weapon;
            character = weapon.getCharacter();
            equippedId = character.getWeapon().getId();
        }
        if (type.equals(ItemType.GEAR)) {
            Gear gear = gearRepository.getOne(itemId);
            item = gear;
            character = gear.getCharacter();
            equippedId = character.getGear().getId();
        }
        if (type.equals(ItemType.OUTFIT)) {
            Outfit outfit = outfitRepository.getOne(itemId);
            item = outfit;
            character = outfit.getCharacter();
            equippedId = character.getOutfit().getId();
        }

        if (item == null) {
            log.error("Item with ID {} not found!", itemId);
            throw new InvalidActionException("Item with this ID was not found!");
        }

        Clan clan = clanRepository.getOne(clanId);

        // check clan and character
        if (character.getId() != characterId || character.getClan().getId() != clanId) {
            log.error("Character with ID {} does not match the requested character!", characterId);
            throw new InvalidActionException("Cannot equip item to the provided character!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character must be READY to un-equip item.");
        }

        // check if item quipped
        if (equippedId != itemId) {
            log.error("Character {} is not carrying the specified item!", characterId);
            throw new InvalidActionException("item with this ID is not equipped by the provided character!");
        }

        // process unequip action
        if (type.equals(ItemType.WEAPON)) {
            unequipWeapon((Weapon) item, character, clan);
        }
        if (type.equals(ItemType.GEAR)) {
            unequipGear((Gear) item, character, clan);
        }
        if (type.equals(ItemType.OUTFIT)) {
            unequipOutfit((Outfit) item, character, clan);
        }
    }

    private void equipWeapon(Weapon weapon, Character character, Clan clan) {
        character.setWeapon(weapon);

        clan.getWeapons().remove(weapon);

        weapon.setClan(null);
        weapon.setCharacter(character);
    }

    private void unequipWeapon(Weapon weapon, Character character, Clan clan) {
        character.setWeapon(null);

        clan.getWeapons().add(weapon);

        weapon.setCharacter(null);
        weapon.setClan(clan);
    }

    private void equipGear(Gear gear, Character character, Clan clan) {
        character.setGear(gear);

        clan.getGear().remove(gear);

        gear.setClan(null);
        gear.setCharacter(character);
    }

    private void unequipGear(Gear gear, Character character, Clan clan) {
        character.setGear(null);

        clan.getGear().add(gear);

        gear.setCharacter(null);
        gear.setClan(clan);
    }

    private void equipOutfit(Outfit outfit, Character character, Clan clan) {
        character.setOutfit(outfit);

        clan.getOutfits().remove(outfit);

        outfit.setClan(null);
        outfit.setCharacter(character);
    }

    private void unequipOutfit(Outfit outfit, Character character, Clan clan) {
        character.setOutfit(null);

        clan.getOutfits().add(outfit);

        outfit.setCharacter(null);
        outfit.setClan(clan);
    }

    @Override
    public void generateItemForCharacter(Character character, ClanNotification notification) {
        log.debug("Generating random item for character {}", character.getId());

        // get random item type
        int diceRoll = randomService.getRandomInt(1, 4);
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
            case 4:
                generateOutfit(character, notification, getRandomRarity());
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

        // load character
        Character character = characterRepository.getOne(characterId);
        if (character == null || character.getClan().getId() != clanId) {
            throw new InvalidActionException("Character not found or doesn't belong to your clan!");
        }

        // load consumable
        Consumable consumable = consumableRepository.getOne(consumableId);
        if (consumable == null || consumable.getClan().getId() != clanId) {
            throw new InvalidActionException("Consumable not found or doesn't belong to your clan!");
        }

        // check prerequizities
        if (consumable.getDetails().getEffectType().equals(EffectType.EXPERIENCE)
                && character.getIntellect() < consumable.getDetails().getPrereq()) {
            throw new InvalidActionException("Character's intellect is too low to perform this action.");
        }

        // process the effect
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
         * Load random outfit details .
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

        gearRepository.save(gear);

        // update notification
        NotificationDetail detail = new NotificationDetail();
        notificationService
                .addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[]{character.getName()},
                        details.getName());
        notification.getDetails().add(detail);
    }

    private void generateOutfit(Character character, ClanNotification notification, ItemDetails.Rarity rarity) {
        // load random outfit details
        List<OutfitDetails> outfitDetailsList = outfitDetailsRepository.findAllByRarity(rarity);
        OutfitDetails details = outfitDetailsList.get(randomService.getRandomInt(0, outfitDetailsList.size() - 1));

        // create outfit
        Outfit outfit = new Outfit();
        outfit.setDetails(details);

        // clan storage
        Clan clan = character.getClan();
        clan.getOutfits().add(outfit);
        outfit.setClan(clan);

        outfitRepository.save(outfit);

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
