package com.withergate.api.service.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.EffectType;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.GearDetails;
import com.withergate.api.model.item.ItemDetails.Rarity;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.OutfitDetails;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.item.ConsumableRepository;
import com.withergate.api.repository.item.GearRepository;
import com.withergate.api.repository.item.ItemDetailsRepository;
import com.withergate.api.repository.item.OutfitRepository;
import com.withergate.api.repository.item.WeaponRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ItemServiceTest {

    private ItemServiceImpl itemService;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private ItemDetailsRepository itemDetailsRepository;

    @Mock
    private WeaponRepository weaponRepository;

    @Mock
    private ConsumableRepository consumableRepository;

    @Mock
    private GearRepository gearRepository;

    @Mock
    private OutfitRepository outfitRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private NotificationService notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        itemService = new ItemServiceImpl(characterRepository, clanRepository, itemDetailsRepository,
                weaponRepository, consumableRepository, gearRepository, outfitRepository,
                randomService, notificationService);
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
        character.setWeapon(null);

        WeaponDetails weaponDetails = new WeaponDetails();

        Weapon weapon = new Weapon();
        weapon.setId(3);
        weapon.setDetails(weaponDetails);
        weapon.setClan(clan);

        Set<Weapon> weapons = new HashSet<>();
        weapons.add(weapon);
        clan.setWeapons(weapons);

        // when equipping weapon
        Mockito.when(weaponRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.equipItem(3, ItemType.WEAPON, 2, 1);

        // then verify entities updated
        assertEquals(character, weapon.getCharacter());
        assertEquals(null, weapon.getClan());
        assertEquals(weapon, character.getWeapon());
        assertEquals(0, clan.getWeapons().size());
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

        WeaponDetails weaponDetails = new WeaponDetails();

        Weapon weapon = new Weapon();
        weapon.setId(3);
        weapon.setDetails(weaponDetails);
        weapon.setClan(null);
        weapon.setCharacter(character);
        character.setWeapon(weapon);

        Set<Weapon> weapons = new HashSet<>();
        clan.setWeapons(weapons);

        // when unequipping weapon
        Mockito.when(weaponRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.unequipItem(3, ItemType.WEAPON, 2, 1);

        // then verify entities updated
        assertEquals(null, weapon.getCharacter());
        assertEquals(clan, weapon.getClan());
        assertEquals(null, character.getWeapon());
        assertEquals(weapon, clan.getWeapons().iterator().next());
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
        character.setWeapon(null);

        WeaponDetails weaponDetails = new WeaponDetails();

        Weapon weapon = new Weapon();
        weapon.setId(3);
        weapon.setDetails(weaponDetails);
        weapon.setClan(clan);

        Set<Weapon> weapons = new HashSet<>();
        weapons.add(weapon);
        clan.setWeapons(weapons);

        // when equipping weapon
        Mockito.when(weaponRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.equipItem(3, ItemType.WEAPON, 2, 1);

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
        character.setWeapon(null);

        OutfitDetails outfitDetails = new OutfitDetails();

        Outfit outfit = new Outfit();
        outfit.setId(3);
        outfit.setDetails(outfitDetails);
        outfit.setClan(clan);

        Set<Outfit> outfits = new HashSet<>();
        outfits.add(outfit);
        clan.setOutfits(outfits);

        // when equipping outfit
        Mockito.when(outfitRepository.getOne(3)).thenReturn(outfit);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.equipItem(3, ItemType.OUTFIT, 2, 1);

        // then verify entities updated
        assertEquals(character, outfit.getCharacter());
        assertEquals(null, outfit.getClan());
        assertEquals(outfit, character.getOutfit());
        assertEquals(0, clan.getOutfits().size());
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

        OutfitDetails outfitDetails = new OutfitDetails();

        Outfit outfit = new Outfit();
        outfit.setId(3);
        outfit.setDetails(outfitDetails);
        outfit.setClan(null);
        outfit.setCharacter(character);
        character.setOutfit(outfit);

        Set<Outfit> outfits = new HashSet<>();
        clan.setOutfits(outfits);

        // when unequipping outfit
        Mockito.when(outfitRepository.getOne(3)).thenReturn(outfit);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.unequipItem(3, ItemType.OUTFIT, 2, 1);

        // then verify entities updated
        assertEquals(null, outfit.getCharacter());
        assertEquals(clan, outfit.getClan());
        assertEquals(null, character.getOutfit());
        assertEquals(outfit, clan.getOutfits().iterator().next());
    }

    @Test
    public void testGivenCharacterAndGearWhenEquippingGearThenVerifyEntitiesUpdated() throws InvalidActionException {
        // given character and gear
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        character.setWeapon(null);

        GearDetails gearDetails = new GearDetails();

        Gear gear = new Gear();
        gear.setId(3);
        gear.setDetails(gearDetails);
        gear.setClan(clan);

        Set<Gear> gears = new HashSet<>();
        gears.add(gear);
        clan.setGear(gears);

        // when equipping gear
        Mockito.when(gearRepository.getOne(3)).thenReturn(gear);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.equipItem(3, ItemType.GEAR, 2, 1);

        // then verify entities updated
        assertEquals(character, gear.getCharacter());
        assertEquals(null, gear.getClan());
        assertEquals(gear, character.getGear());
        assertEquals(0, clan.getGear().size());
    }

    @Test
    public void testGivenCharacterAndGearWhenUnequippingGearThenVerifyEntitiesUpdated() throws InvalidActionException {
        // given character and gear
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(2);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);

        GearDetails gearDetails = new GearDetails();

        Gear gear = new Gear();
        gear.setId(3);
        gear.setDetails(gearDetails);
        gear.setClan(null);
        gear.setCharacter(character);
        character.setGear(gear);

        Set<Gear> gears = new HashSet<>();
        clan.setGear(gears);

        // when unequipping gear
        Mockito.when(gearRepository.getOne(3)).thenReturn(gear);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.unequipItem(3, ItemType.GEAR, 2, 1);

        // then verify entities updated
        assertEquals(null, gear.getCharacter());
        assertEquals(clan, gear.getClan());
        assertEquals(null, character.getGear());
        assertEquals(gear, clan.getGear().iterator().next());
    }

    @Test
    public void testGivenCharacterWhenCraftingWeaponThenVerifyWeaponSaved() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setCraftsmanship(4);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(50);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        List<WeaponDetails> detailsList = new ArrayList<>(0);
        WeaponDetails details = new WeaponDetails();
        details.setCraftable(true);
        details.setIdentifier("Knife");
        detailsList.add(details);
        Mockito.when(itemDetailsRepository.findWeaponDetailsByRarityAndCraftable(Rarity.COMMON, true))
                .thenReturn(detailsList);

        // when crafting weapon
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableItem(character, 1, notification, ItemType.WEAPON);

        // then verify weapon saved
        ArgumentCaptor<Weapon> captor = ArgumentCaptor.forClass(Weapon.class);
        Mockito.verify(weaponRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getDetails());
    }

    @Test
    public void testGivenCharacterWhenCraftingOutfitThenVerifyOutfitSaved() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setCraftsmanship(4);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(1); // rare
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        List<OutfitDetails> detailsList = new ArrayList<>(0);
        OutfitDetails details = new OutfitDetails();
        details.setCraftable(true);
        details.setIdentifier("Rare rags");
        details.setRarity(Rarity.RARE);
        detailsList.add(details);
        Mockito.when(itemDetailsRepository.findOutfitDetailsByRarityAndCraftable(Rarity.RARE, true))
                .thenReturn(detailsList);

        // when crafting outfit
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableItem(character, 1, notification, ItemType.OUTFIT);

        // then verify outfit saved
        ArgumentCaptor<Outfit> captor = ArgumentCaptor.forClass(Outfit.class);
        Mockito.verify(outfitRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getDetails());
    }

    @Test
    public void testGivenCharacterWhenCraftingGearThenVerifyOutfitSaved() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setCraftsmanship(4);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(90);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        List<GearDetails> detailsList = new ArrayList<>(0);
        GearDetails details = new GearDetails();
        details.setCraftable(true);
        details.setIdentifier("Gear");
        detailsList.add(details);
        Mockito.when(itemDetailsRepository.findGearDetailsByRarityAndCraftable(Rarity.COMMON, true))
                .thenReturn(detailsList);

        // when crafting gear
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableItem(character, 1, notification, ItemType.GEAR);

        // then verify gear saved
        ArgumentCaptor<Gear> captor = ArgumentCaptor.forClass(Gear.class);
        Mockito.verify(gearRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getDetails());
    }

    @Test
    public void testGivenCharacterWhenGeneratingRandomItemWithHighRollThenVerifyConsumableGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setConsumables(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(80); // rarity
        Mockito.when(randomService.getRandomItemType()).thenReturn(ItemType.CONSUMABLE);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0); // item index

        List<ConsumableDetails> detailsList = new ArrayList<>(0);
        ConsumableDetails details = new ConsumableDetails();
        details.setIdentifier("Medkit");
        details.setEffectType(EffectType.HEALING);
        detailsList.add(details);

        Mockito.when(itemDetailsRepository.findConsumableDetailsByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify consumable generated
        assertEquals(details, clan.getConsumables().iterator().next().getDetails());
    }

    @Test
    public void testGivenCharacterWhenGeneratingRandomWeaponThenVerifyWeaponGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setWeapons(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setWeapon(new Weapon());

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(20); // rarity
        Mockito.when(randomService.getRandomItemType()).thenReturn(ItemType.WEAPON);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0); // item index

        List<WeaponDetails> detailsList = new ArrayList<>(0);
        WeaponDetails details = new WeaponDetails();
        details.setIdentifier("Knife");
        detailsList.add(details);

        Mockito.when(itemDetailsRepository.findWeaponDetailsByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify weapon generated
        assertEquals(details, clan.getWeapons().iterator().next().getDetails());
    }

    @Test
    public void testGivenCharacterWhenGeneratingRandomOutfitThenVerifyOutfitGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setOutfits(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(20); // rarity
        Mockito.when(randomService.getRandomItemType()).thenReturn(ItemType.OUTFIT);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0); // item index

        List<OutfitDetails> detailsList = new ArrayList<>(0);
        OutfitDetails details = new OutfitDetails();
        details.setIdentifier("Rags");
        detailsList.add(details);

        Mockito.when(itemDetailsRepository.findOutfitDetailsByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify outfit generated
        assertEquals(details, clan.getOutfits().iterator().next().getDetails());
    }

    @Test
    public void testGivenCharacterWhenGeneratingRandomGearThenVerifyGearGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setGear(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(20); // rarity
        Mockito.when(randomService.getRandomItemType()).thenReturn(ItemType.GEAR);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0); // item index

        List<GearDetails> detailsList = new ArrayList<>(0);
        GearDetails details = new GearDetails();
        details.setIdentifier("Gear");
        detailsList.add(details);

        Mockito.when(itemDetailsRepository.findGearDetailsByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify gear generated
        assertEquals(details, clan.getGear().iterator().next().getDetails());
    }

    @Test
    public void testGivenCharacterWhenGeneratingRandomConsumableThenVerifyConsumableGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setConsumables(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(20); // rarity
        Mockito.when(randomService.getRandomItemType()).thenReturn(ItemType.CONSUMABLE);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0); // item index

        List<ConsumableDetails> detailsList = new ArrayList<>(0);
        ConsumableDetails details = new ConsumableDetails();
        details.setIdentifier("Consumable");
        detailsList.add(details);

        Mockito.when(itemDetailsRepository.findConsumableDetailsByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify consumable generated
        assertEquals(details, clan.getConsumables().iterator().next().getDetails());
    }

    @Test
    public void testGivenItemsWhenLoadingByTypeThenVerifyCorrectItemsLoaded() {
        // given items
        Weapon weapon = new Weapon();
        weapon.setId(1);
        Mockito.when(weaponRepository.getOne(1)).thenReturn(weapon);

        Outfit outfit = new Outfit();
        outfit.setId(1);
        Mockito.when(outfitRepository.getOne(1)).thenReturn(outfit);

        Gear gear = new Gear();
        gear.setId(1);
        Mockito.when(gearRepository.getOne(1)).thenReturn(gear);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when loading by type then assert correct item loaded
        Assert.assertEquals(weapon, itemService.loadItemByType(1, ItemType.WEAPON));
        Assert.assertEquals(outfit, itemService.loadItemByType(1, ItemType.OUTFIT));
        Assert.assertEquals(gear, itemService.loadItemByType(1, ItemType.GEAR));
        Assert.assertEquals(consumable, itemService.loadItemByType(1, ItemType.CONSUMABLE));
    }

    @Test
    public void testGivenItemDetailsWhenLoadingByTypeThenVerifyCorrectDetailsLoaded() {
        // given items
        WeaponDetails weaponDetails = new WeaponDetails();
        weaponDetails.setItemType(ItemType.WEAPON);
        Weapon weapon = new Weapon();
        weapon.setId(1);
        weapon.setDetails(weaponDetails);
        Mockito.when(weaponRepository.getOne(1)).thenReturn(weapon);

        OutfitDetails outfitDetails = new OutfitDetails();
        outfitDetails.setItemType(ItemType.OUTFIT);
        Outfit outfit = new Outfit();
        outfit.setId(1);
        outfit.setDetails(outfitDetails);
        Mockito.when(outfitRepository.getOne(1)).thenReturn(outfit);

        GearDetails gearDetails = new GearDetails();
        gearDetails.setItemType(ItemType.GEAR);
        Gear gear = new Gear();
        gear.setId(1);
        gear.setDetails(gearDetails);
        Mockito.when(gearRepository.getOne(1)).thenReturn(gear);

        ConsumableDetails consumableDetails = new ConsumableDetails();
        consumableDetails.setItemType(ItemType.CONSUMABLE);
        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(consumableDetails);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when loading by type then assert correct details loaded
        Assert.assertEquals(weaponDetails, itemService.loadItemDetailsByType(1, ItemType.WEAPON));
        Assert.assertEquals(outfitDetails, itemService.loadItemDetailsByType(1, ItemType.OUTFIT));
        Assert.assertEquals(gearDetails, itemService.loadItemDetailsByType(1, ItemType.GEAR));
        Assert.assertEquals(consumableDetails, itemService.loadItemDetailsByType(1, ItemType.CONSUMABLE));
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

        ConsumableDetails details = new ConsumableDetails();
        details.setEffectType(EffectType.HEALING);
        details.setEffect(2);
        details.setPrereq(0);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.useConsumable(1, 1, 1);

        // then verify character healed and consumable deleted
        Assert.assertEquals(7, character.getHitpoints());
        Mockito.verify(consumableRepository).delete(consumable);
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

        ConsumableDetails details = new ConsumableDetails();
        details.setEffectType(EffectType.EXPERIENCE);
        details.setEffect(5);
        details.setPrereq(3);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.useConsumable(1, 1, 1);

        // then verify character gained experience and consumable deleted
        Assert.assertEquals(5, character.getExperience());
        Mockito.verify(consumableRepository).delete(consumable);
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

        ConsumableDetails details = new ConsumableDetails();
        details.setEffectType(EffectType.BUFF_COMBAT);
        details.setEffect(1);
        details.setPrereq(0);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.useConsumable(1, 1, 1);

        // then verify character combat increased and consumable deleted
        Assert.assertEquals(6, character.getCombat());
        Mockito.verify(consumableRepository).delete(consumable);
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

        ConsumableDetails details = new ConsumableDetails();
        details.setEffectType(EffectType.BUFF_SCAVENGE);
        details.setEffect(1);
        details.setPrereq(0);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.useConsumable(1, 1, 1);

        // then verify character scavenge increased and consumable deleted
        Assert.assertEquals(6, character.getScavenge());
        Mockito.verify(consumableRepository).delete(consumable);
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

        ConsumableDetails details = new ConsumableDetails();
        details.setEffectType(EffectType.BUFF_CRAFTSMANSHIP);
        details.setEffect(1);
        details.setPrereq(0);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.useConsumable(1, 1, 1);

        // then verify character craftsmanship increased and consumable deleted
        Assert.assertEquals(6, character.getCraftsmanship());
        Mockito.verify(consumableRepository).delete(consumable);
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

        ConsumableDetails details = new ConsumableDetails();
        details.setEffectType(EffectType.BUFF_INTELLECT);
        details.setEffect(1);
        details.setPrereq(0);

        Consumable consumable = new Consumable();
        consumable.setId(1);
        consumable.setDetails(details);
        consumable.setClan(clan);
        Mockito.when(consumableRepository.getOne(1)).thenReturn(consumable);

        // when using consumable
        itemService.useConsumable(1, 1, 1);

        // then verify character intellect increased and consumable deleted
        Assert.assertEquals(6, character.getIntellect());
        Mockito.verify(consumableRepository).delete(consumable);
    }

}
