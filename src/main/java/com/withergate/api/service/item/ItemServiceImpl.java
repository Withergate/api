package com.withergate.api.service.item;

import java.util.List;

import javax.annotation.Nullable;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.item.EffectType;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemCost;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemDetails.Rarity;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.game.repository.item.ItemDetailsRepository;
import com.withergate.api.game.repository.item.ItemRepository;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.profile.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Item service.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private static final int EPIC_ITEM_CHANCE = 5;
    private static final int RARE_ITEM_CHANCE = 20;

    private final CharacterRepository characterRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemRepository itemRepository;
    private final RandomService randomService;
    private final NotificationService notificationService;
    private AchievementService achievementService;

    @Autowired
    public void setAchievementService(@Lazy AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @Override
    public Item loadItem(int itemId) {
        return itemRepository.getOne(itemId);
    }

    @Transactional
    @Override
    public void equipItem(int itemId, int characterId, int clanId) throws InvalidActionException {
        Item item = itemRepository.getOne(itemId);
        Character character = characterRepository.getOne(characterId);

        if (character.getItem(item.getDetails().getItemType()) != null) {
            throw new InvalidActionException("error.item-equip", "Character is already holding an item of this type!");
        }

        if (item.getDetails().getItemType().equals(ItemType.CONSUMABLE)) {
            useConsumable(item, character);
            return;
        }

        // check clan
        if (item.getClan() == null || item.getClan().getId() != clanId) {
            log.error("Item with ID {} is not available in the clan storage of clan ID {}!", itemId, clanId);
            throw new InvalidActionException(null, "Item with this ID does not belong the the provided clan!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("error.ready", "Character must be READY to equip item.");
        }

        item.setCharacter(character);
        character.getItems().add(item);
        item.getClan().getItems().remove(item);
        item.setClan(null);
    }

    @Transactional
    @Override
    public void unequipItem(int itemId, int characterId, int clanId) throws InvalidActionException {
        Item item = itemRepository.getOne(itemId);
        Character character = characterRepository.getOne(characterId);

        // check clan and character
        if (character.getId() != characterId || character.getClan().getId() != clanId) {
            log.error("Character with ID {} does not match the requested character!", characterId);
            throw new InvalidActionException(null, "Cannot equip item to the provided character!");
        }

        // check if item quipped
        if (item.getCharacter().getId() != characterId) {
            log.error("Character {} is not carrying the specified item!", characterId);
            throw new InvalidActionException(null, "Item with this ID is not equipped by the provided character!");
        }

        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("error.ready", "Character must be READY to unequip item.");
        }

        // process unequip action
        item.setCharacter(null);
        character.getItems().remove(item);
        item.setClan(character.getClan());
        character.getClan().getItems().add(item);
    }

    @Override
    public void generateItemForCharacter(Character character, ClanNotification notification, @Nullable String itemId,
                                         @Nullable Rarity rarity, int turn) {
        log.debug("Generating random item for character {}", character.getId());

        ItemDetails details;
        if (itemId != null) {
            details = itemDetailsRepository.getOne(itemId);
        } else {
            // get random item type
            ItemType type = randomService.getRandomItemType();
            if (rarity == null) {
                rarity = getRandomRarity();
            }
            log.debug("Getting random item with rarity {}, type {} and turn less than {}.", rarity, type, turn);
            List<ItemDetails> detailsList = itemDetailsRepository.findItemDetailsByRarityAndItemTypeAndTurnLessThan(rarity, type, turn);
            // if epic items are not available for given turn, find common instead
            if (detailsList.isEmpty()) {
                detailsList = itemDetailsRepository.findItemDetailsByRarityAndItemTypeAndTurnLessThan(Rarity.COMMON, type, turn);
            }
            details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));
        }

        // create item
        Item item = new Item();
        item.setDetails(details);

        // add to storage
        Clan clan = character.getClan();
        clan.getItems().add(item);
        item.setClan(clan);
        notification.setItem(true);

        itemRepository.save(item);

        // update notification
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.item.found.storage", new String[] {character.getName()},
                details.getName());
        notification.getDetails().add(detail);
    }

    @Override
    public Item generateCraftableItem(Character character, ClanNotification notification, ItemDetails details) {
        log.debug("Crafting item with {}", character.getName());

        // save item
        Item item = new Item();
        item.setDetails(details);
        item.setClan(character.getClan());
        character.getClan().getItems().add(item);
        notification.setItem(true);
        itemRepository.save(item);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.character.crafting", new String[] {character.getName()},
                details.getName());
        notification.getDetails().add(detail);

        // update statistics
        Clan clan = character.getClan();
        clan.getStatistics().setCraftedItems(clan.getStatistics().getCraftedItems() + 1);
        achievementService.checkAchievementAward(clan.getId(), AchievementType.CRAFT_COUNT, clan.getStatistics().getCraftedItems());

        return item;
    }

    @Override
    public Item generateRandomItem(int turn) {
        ItemDetails details = getRandomItemDetails(randomService.getRandomItemType(), getRandomRarity(), turn);
        Item item = new Item();
        item.setDetails(details);
        return itemRepository.save(item);
    }

    private void useConsumable(Item consumable, Character character) throws InvalidActionException {
        log.debug("Using consumable {} with character {}", consumable.getId(), character.getId());

        if (consumable.getClan().getId() != character.getClan().getId()) {
            throw new InvalidActionException(null, "Consumable not found or doesn't belong to your clan!");
        }

        if (!consumable.getDetails().getItemType().equals(ItemType.CONSUMABLE)) {
            throw new InvalidActionException(null, "The provided item is not a consumable!");
        }

        // check prerequizities
        if (consumable.getDetails().getEffectType().equals(EffectType.EXPERIENCE)
                && character.getIntellect() < consumable.getDetails().getPrereq()) {
            throw new InvalidActionException("error.item-intellect", "Character's intellect is too low to perform this action.");
        }

        // process the effect
        switch (consumable.getDetails().getEffectType()) {
            case HEALING:
                int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();
                if (hitpointsMissing == 0) {
                    throw new InvalidActionException(null, "This character already has full health!");
                }
                int healing = Math.min(hitpointsMissing, consumable.getDetails().getBonus());
                character.setHitpoints(character.getHitpoints() + healing);
                break;
            case EXPERIENCE:
                character.setExperience(character.getExperience() + consumable.getDetails().getBonus());
                break;
            case BUFF_COMBAT:
                if (character.getCombat() > 6 || character.getHitpoints() < character.getMaxHitpoints()) {
                    throw new InvalidActionException("error-item-max-stat",
                            "This character is injured or already reached max combat value!");
                }
                character.setCombat(character.getCombat() + 1);
                break;
            case BUFF_SCAVENGE:
                if (character.getScavenge() > 6 || character.getHitpoints() < character.getMaxHitpoints()) {
                    throw new InvalidActionException("error-item-max-stat",
                            "This character is injured or already reached max scavenge value!");
                }
                character.setScavenge(character.getScavenge() + 1);
                break;
            case BUFF_CRAFTSMANSHIP:
                if (character.getCraftsmanship() > 6) {
                    throw new InvalidActionException("error-item-max-stat",
                            "This character is injured or already reached max craftsmanship value!");
                }
                character.setCraftsmanship(character.getCraftsmanship() + 1);
                break;
            case BUFF_INTELLECT:
                if (character.getIntellect() > 6) {
                    throw new InvalidActionException("error-item-max-stat",
                            "This character is injured or already reached max intellect value!");
                }
                character.setIntellect(character.getIntellect() + 1);
                break;
            default:
                log.error("Unknown consumable type: {}!", consumable.getDetails().getEffectType());
        }

        // delete consumable
        deleteItem(consumable);
    }

    @Override
    public void deleteItem(Item item) {
        if (item.getOffer() != null) {
            item.getOffer().setItem(null);
            item.setOffer(null);
        }

        item.setClan(null);
        item.setCharacter(null);
        itemRepository.delete(item);
    }

    @Override
    public void deleteItem(Character character, ItemCost itemCost, ClanNotification notification) {
        Item item = null;
        switch (itemCost) {
            case ANY:
                item = character.getItems().iterator().next();
                break;
            case WEAPON:
                item = character.getWeapon();
                break;
            case OUTFIT:
                item = character.getOutfit();
                break;
            case GEAR:
                item = character.getGear();
                break;
            default:
                log.error("Unknown item type: {}", itemCost.name());
        }

        if (item == null) {
            log.error("Attempting to delete null item!");
            return;
        }

        // update notification
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.item.deleted", new String[]{}, item.getDetails().getName());
        notification.getDetails().add(detail);

        // delete item
        deleteItem(item);
    }

    private ItemDetails.Rarity getRandomRarity() {
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (diceRoll < EPIC_ITEM_CHANCE) {
            return Rarity.EPIC;
        } else if (diceRoll < RARE_ITEM_CHANCE) {
            return Rarity.RARE;
        } else {
            return Rarity.COMMON;
        }
    }

    private ItemDetails.Rarity getRandomRarity(int craftsmanship, int bonus) {
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);

        // epic items cannot be crafted, only found
        if (diceRoll < (craftsmanship * 5 + bonus * 5)) {
            return ItemDetails.Rarity.RARE;
        } else {
            return ItemDetails.Rarity.COMMON;
        }
    }

    private ItemDetails getRandomItemDetails(ItemType type, Rarity rarity, int turn) {
        List<ItemDetails> detailsList = itemDetailsRepository.findItemDetailsByRarityAndItemTypeAndTurnLessThan(rarity, type, turn);
        if (detailsList.isEmpty()) {
            detailsList = itemDetailsRepository.findItemDetailsByRarityAndItemTypeAndTurnLessThan(Rarity.COMMON, type, turn);
        }

        return detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));
    }

}
