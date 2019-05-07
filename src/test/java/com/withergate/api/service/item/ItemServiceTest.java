package com.withergate.api.service.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.EffectType;
import com.withergate.api.model.item.ItemDetails.Rarity;
import com.withergate.api.model.item.ItemType;
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

        GameProperties gameProperties = new GameProperties();
        gameProperties.setRareItemChance(10);

        itemService = new ItemServiceImpl(characterRepository, clanRepository, itemDetailsRepository,
                weaponRepository, consumableRepository, gearRepository, outfitRepository,
                randomService, gameProperties, notificationService);
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

        // when equipping weapon
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
    public void testGivenCharacterWithWeaponWhenGeneratingRandomItemWithLowRollThenVerifyWeaponGenerated() {
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

}
