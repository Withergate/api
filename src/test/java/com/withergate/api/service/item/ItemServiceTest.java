package com.withergate.api.service.item;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.item.EffectType;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemDetails.Rarity;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.game.repository.item.ItemDetailsRepository;
import com.withergate.api.game.repository.item.ItemRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.profile.AchievementService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ItemServiceTest {

    private ItemServiceImpl itemService;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private ItemDetailsRepository itemDetailsRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AchievementService achievementService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        itemService = new ItemServiceImpl(characterRepository, itemDetailsRepository,
                itemRepository, randomService, notificationService);
        itemService.setAchievementService(achievementService);
    }

    @Test
    public void testGivenCharacterAndWeaponWhenEquippingWeaponThenVerifyEntitiesUpdated() throws InvalidActionException {
        // given character and weapon
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);

        ItemDetails weaponDetails = new ItemDetails();
        weaponDetails.setItemType(ItemType.WEAPON);

        Item weapon = new Item();
        weapon.setId(3);
        weapon.setDetails(weaponDetails);
        weapon.setClan(clan);

        Set<Item> items = new HashSet<>();
        items.add(weapon);
        clan.setItems(items);

        // when equipping weapon
        Mockito.when(itemRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);

        itemService.equipItem(3,2, 1);

        // then verify entities updated
        assertEquals(character, weapon.getCharacter());
        assertNull(weapon.getClan());
        assertEquals(weapon, character.getWeapon());
        assertEquals(0, clan.getItems().size());
    }

    @Test
    public void testGivenCharacterAndWeaponWhenUnequippingWeaponThenVerifyEntitiesUpdated() throws InvalidActionException {
        // given character and weapon
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);

        ItemDetails weaponDetails = new ItemDetails();
        weaponDetails.setItemType(ItemType.WEAPON);

        Item weapon = new Item();
        weapon.setId(3);
        weapon.setDetails(weaponDetails);
        weapon.setClan(null);
        weapon.setCharacter(character);
        character.getItems().add(weapon);

        Set<Item> weapons = new HashSet<>();
        clan.setItems(weapons);

        // when unequipping weapon
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(itemRepository.getOne(3)).thenReturn(weapon);

        itemService.unequipItem(3,2, 1);

        // then verify entities updated
        assertNull(weapon.getCharacter());
        assertEquals(clan, weapon.getClan());
        assertNull(character.getWeapon());
        assertEquals(weapon, clan.getItems().iterator().next());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenWeaponWhenEquippingWeaponToCharacterFromDifferentClanThenVerifyExceptionThrown() throws InvalidActionException {
        // given character and weapon
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setClan(clan);

        ItemDetails weaponDetails = new ItemDetails();
        weaponDetails.setItemType(ItemType.WEAPON);

        Item weapon = new Item();
        weapon.setId(3);
        weapon.setDetails(weaponDetails);
        weapon.setClan(clan);

        Set<Item> weapons = new HashSet<>();
        weapons.add(weapon);
        clan.setItems(weapons);

        // when equipping weapon
        Mockito.when(itemRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);

        itemService.equipItem(3,2, 1);

        // then verify exception thrown
    }

    @Test
    public void testGivenCharacterAndOutfitWhenEquippingOutfitThenVerifyEntitiesUpdated() throws InvalidActionException {
        // given character and outfit
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);

        ItemDetails outfitDetails = new ItemDetails();
        outfitDetails.setItemType(ItemType.OUTFIT);

        Item outfit = new Item();
        outfit.setId(3);
        outfit.setDetails(outfitDetails);
        outfit.setClan(clan);

        Set<Item> outfits = new HashSet<>();
        outfits.add(outfit);
        clan.setItems(outfits);

        // when equipping outfit
        Mockito.when(itemRepository.getOne(3)).thenReturn(outfit);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);

        itemService.equipItem(3,2, 1);

        // then verify entities updated
        assertEquals(character, outfit.getCharacter());
        assertNull(outfit.getClan());
        assertEquals(outfit, character.getOutfit());
        assertEquals(0, clan.getItems().size());
    }

    @Test
    public void testGivenCharacterAndOutfitWhenUnequippingOutfitThenVerifyEntitiesUpdated() throws InvalidActionException {
        // given character and outfit
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);

        ItemDetails outfitDetails = new ItemDetails();
        outfitDetails.setItemType(ItemType.OUTFIT);

        Item outfit = new Item();
        outfit.setId(3);
        outfit.setDetails(outfitDetails);
        outfit.setClan(null);
        outfit.setCharacter(character);
        character.getItems().add(outfit);

        Set<Item> outfits = new HashSet<>();
        clan.setItems(outfits);

        // when unequipping outfit
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(itemRepository.getOne(3)).thenReturn(outfit);

        itemService.unequipItem(3,2, 1);

        // then verify entities updated
        assertNull(outfit.getCharacter());
        assertEquals(clan, outfit.getClan());
        assertNull(character.getOutfit());
        assertEquals(outfit, clan.getItems().iterator().next());
    }

    @Test
    public void testGivenCharacterWhenCraftingWeaponThenVerifyWeaponSaved() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setCraftsmanship(4);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        List<ItemDetails> detailsList = new ArrayList<>(0);
        ItemDetails details = new ItemDetails();
        details.setIdentifier("Knife");
        detailsList.add(details);

        // when crafting weapon
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableItem(character, notification, details);

        // then verify weapon saved
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(itemRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getDetails());
    }

    @Test
    public void testGivenCharacterWhenCraftingOutfitThenVerifyOutfitSaved() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setCraftsmanship(4);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        ItemDetails details = new ItemDetails();
        details.setIdentifier("Rare rags");
        details.setRarity(Rarity.RARE);

        // when crafting outfit
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableItem(character, notification, details);

        // then verify outfit saved
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(itemRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getDetails());
    }

    @Test
    public void testGivenCharacterWhenCraftingGearThenVerifyOutfitSaved() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setCraftsmanship(4);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(90);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        ItemDetails details = new ItemDetails();
        details.setIdentifier("Gear");

        // when crafting gear
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableItem(character, notification, details);

        // then verify gear saved
        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(itemRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getDetails());
    }

    @Test
    public void testGivenCharacterWhenGeneratingRandomItemWithHighRollThenVerifyConsumableGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setItems(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(80); // rarity
        Mockito.when(randomService.getRandomItemType()).thenReturn(ItemType.CONSUMABLE);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0); // item index

        List<ItemDetails> detailsList = new ArrayList<>(0);
        ItemDetails details = new ItemDetails();
        details.setIdentifier("Medkit");
        details.setEffectType(EffectType.HEALING);
        detailsList.add(details);

        Mockito.when(itemDetailsRepository.findItemDetailsByRarityAndItemTypeAndTurnLessThan(Rarity.COMMON, ItemType.CONSUMABLE, 1))
                .thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification, null, 1);

        // then verify consumable generated
        assertEquals(details, clan.getItems().iterator().next().getDetails());
    }

    @Test
    public void testGivenCharacterWhenUsingHealingConsumableThenVerifyCharacterHealed() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Quick Pete");
        character.setHitpoints(5);
        character.setMaxHitpoints(10);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        ItemDetails details = new ItemDetails();
        details.setEffectType(EffectType.HEALING);
        details.setBonus(2);
        details.setPrereq(0);
        details.setItemType(ItemType.CONSUMABLE);

        Item consumable = new Item();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(itemRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.equipItem(1, 1, 1);

        // then verify character healed and consumable deleted
        Assert.assertEquals(7, character.getHitpoints());
        Mockito.verify(itemRepository).delete(consumable);
    }

    @Test
    public void testGivenCharacterWhenUsingExperienceConsumableThenVerifyExperienceGained() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Quick Pete");
        character.setExperience(0);
        character.setIntellect(3);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        ItemDetails details = new ItemDetails();
        details.setEffectType(EffectType.EXPERIENCE);
        details.setBonus(5);
        details.setPrereq(3);
        details.setItemType(ItemType.CONSUMABLE);

        Item consumable = new Item();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(itemRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.equipItem(1, 1, 1);

        // then verify character gained experience and consumable deleted
        Assert.assertEquals(5, character.getExperience());
        Mockito.verify(itemRepository).delete(consumable);
    }

    @Test
    public void testGivenCharacterWhenUsingCombatBuffConsumableThenVerifyCombatIncreased() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Quick Pete");
        character.setCombat(5);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        ItemDetails details = new ItemDetails();
        details.setEffectType(EffectType.BUFF_COMBAT);
        details.setBonus(1);
        details.setPrereq(0);
        details.setItemType(ItemType.CONSUMABLE);

        Item consumable = new Item();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(itemRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.equipItem(1, 1, 1);

        // then verify character combat increased and consumable deleted
        Assert.assertEquals(6, character.getCombat());
        Mockito.verify(itemRepository).delete(consumable);
    }

    @Test
    public void testGivenCharacterWhenUsingScavengeBuffConsumableThenVerifyScavengeIncreased() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Quick Pete");
        character.setScavenge(5);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        ItemDetails details = new ItemDetails();
        details.setEffectType(EffectType.BUFF_SCAVENGE);
        details.setBonus(1);
        details.setPrereq(0);
        details.setItemType(ItemType.CONSUMABLE);

        Item consumable = new Item();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(itemRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.equipItem(1, 1, 1);

        // then verify character scavenge increased and consumable deleted
        Assert.assertEquals(6, character.getScavenge());
        Mockito.verify(itemRepository).delete(consumable);
    }

    @Test
    public void testGivenCharacterWhenUsingCraftsmanshipBuffConsumableThenVerifyCraftsmanshipIncreased() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Quick Pete");
        character.setCraftsmanship(5);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        ItemDetails details = new ItemDetails();
        details.setEffectType(EffectType.BUFF_CRAFTSMANSHIP);
        details.setBonus(1);
        details.setPrereq(0);
        details.setItemType(ItemType.CONSUMABLE);

        Item consumable = new Item();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(itemRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.equipItem(1, 1, 1);

        // then verify character craftsmanship increased and consumable deleted
        Assert.assertEquals(6, character.getCraftsmanship());
        Mockito.verify(itemRepository).delete(consumable);
    }

    @Test
    public void testGivenCharacterWhenUsingIntellectBuffConsumableThenVerifyIntellectIncreased() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Quick Pete");
        character.setIntellect(5);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        ItemDetails details = new ItemDetails();
        details.setEffectType(EffectType.BUFF_INTELLECT);
        details.setBonus(1);
        details.setPrereq(0);
        details.setItemType(ItemType.CONSUMABLE);

        Item consumable = new Item();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(itemRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.equipItem(1, 1, 1);

        // then verify character intellect increased and consumable deleted
        Assert.assertEquals(6, character.getIntellect());
        Mockito.verify(itemRepository).delete(consumable);
    }

}
