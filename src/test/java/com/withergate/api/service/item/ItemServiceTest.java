package com.withergate.api.service.item;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.EffectType;
import com.withergate.api.model.item.ItemDetails.Rarity;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.notification.ClanNotification;
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

import java.util.ArrayList;
import java.util.List;
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
    private WeaponRepository weaponRepository;

    @Mock
    private WeaponDetailsRepository weaponDetailsRepository;

    @Mock
    private ConsumableRepository consumableRepository;

    @Mock
    private ConsumableDetailsRepository consumableDetailsRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private NotificationService notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties gameProperties = new GameProperties();
        gameProperties.setRareItemChance(10);

        itemService = new ItemServiceImpl(characterRepository, clanRepository, weaponRepository, weaponDetailsRepository, consumableRepository,
                consumableDetailsRepository, randomService, gameProperties, notificationService);
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

        List<Weapon> weapons = new ArrayList<>();
        weapons.add(weapon);
        clan.setWeapons(weapons);

        // when equipping weapon
        Mockito.when(weaponRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.equipWeapon(3, 2, 1);

        // then verify entities updated
        ArgumentCaptor<Weapon> weaponCaptor = ArgumentCaptor.forClass(Weapon.class);
        ArgumentCaptor<Character> characterCaptor = ArgumentCaptor.forClass(Character.class);
        ArgumentCaptor<Clan> clanCaptor = ArgumentCaptor.forClass(Clan.class);

        Mockito.verify(weaponRepository).save(weaponCaptor.capture());
        Mockito.verify(characterRepository).save(characterCaptor.capture());
        Mockito.verify(clanRepository).save(clanCaptor.capture());

        assertEquals(character, weaponCaptor.getValue().getCharacter());
        assertEquals(null, weaponCaptor.getValue().getClan());
        assertEquals(weapon, characterCaptor.getValue().getWeapon());
        assertEquals(0, clanCaptor.getValue().getWeapons().size());
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

        List<Weapon> weapons = new ArrayList<>();
        clan.setWeapons(weapons);

        // when equipping weapon
        Mockito.when(weaponRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.unequipWeapon(3, 2, 1);

        // then verify entities updated
        ArgumentCaptor<Weapon> weaponCaptor = ArgumentCaptor.forClass(Weapon.class);
        ArgumentCaptor<Character> characterCaptor = ArgumentCaptor.forClass(Character.class);
        ArgumentCaptor<Clan> clanCaptor = ArgumentCaptor.forClass(Clan.class);

        Mockito.verify(weaponRepository).save(weaponCaptor.capture());
        Mockito.verify(characterRepository).save(characterCaptor.capture());
        Mockito.verify(clanRepository).save(clanCaptor.capture());

        assertEquals(null, weaponCaptor.getValue().getCharacter());
        assertEquals(clan, weaponCaptor.getValue().getClan());
        assertEquals(null, characterCaptor.getValue().getWeapon());
        assertEquals(weapon, clanCaptor.getValue().getWeapons().get(0));
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

        List<Weapon> weapons = new ArrayList<>();
        weapons.add(weapon);
        clan.setWeapons(weapons);

        // when equipping weapon
        Mockito.when(weaponRepository.getOne(3)).thenReturn(weapon);
        Mockito.when(characterRepository.getOne(2)).thenReturn(character);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        itemService.equipWeapon(3, 2, 1);

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
        Mockito.when(weaponDetailsRepository.findAllByRarityAndCraftable(Rarity.COMMON, true)).thenReturn(detailsList);

        // when crafting weapon
        ClanNotification notification = new ClanNotification();
        itemService.generateCraftableWeapon(character, 1, notification);

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
        clan.setConsumables(new ArrayList<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(80);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        List<ConsumableDetails> detailsList = new ArrayList<>(0);
        ConsumableDetails details = new ConsumableDetails();
        details.setIdentifier("Medkit");
        details.setEffectType(EffectType.HEALING);
        detailsList.add(details);

        Mockito.when(consumableDetailsRepository.findAllByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify consumable generated
        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getConsumables().get(0).getDetails());
    }

    @Test
    public void testGivenCharacterWithWeaponWhenGeneratingRandomItemWithLowRollThenVerifyWeaponGenerated() {
        // given character
        Clan clan = new Clan();
        clan.setId(2);
        clan.setName("Dragons");
        clan.setWeapons(new ArrayList<>());

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setWeapon(new Weapon());

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(20,80);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        List<WeaponDetails> detailsList = new ArrayList<>(0);
        WeaponDetails details = new WeaponDetails();
        details.setIdentifier("Knife");
        detailsList.add(details);

        Mockito.when(weaponDetailsRepository.findAllByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify weapon generated
        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getWeapons().get(0).getDetails());
    }

    @Test
    public void testGivenCharacterWithoutWeaponWhenGeneratingRandomItemWithLowRollThenVerifyWeaponGeneratedAndEquipped() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(20,80);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        List<WeaponDetails> detailsList = new ArrayList<>(0);
        WeaponDetails details = new WeaponDetails();
        details.setIdentifier("Knife");
        detailsList.add(details);

        Mockito.when(weaponDetailsRepository.findAllByRarity(Rarity.COMMON)).thenReturn(detailsList);

        // when generating random item
        ClanNotification notification = new ClanNotification();
        itemService.generateItemForCharacter(character, notification);

        // then verify weapon generated and equipped
        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);
        Mockito.verify(characterRepository).save(captor.capture());
        assertEquals(details, captor.getValue().getWeapon().getDetails());
    }

}
