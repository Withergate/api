package com.withergate.api.service.item;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.item.ConsumableDetailsRepository;
import com.withergate.api.repository.item.ConsumableRepository;
import com.withergate.api.repository.item.WeaponDetailsRepository;
import com.withergate.api.repository.item.WeaponRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.exception.InvalidActionException;

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

    private ItemService itemService;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties gameProperties = new GameProperties();
        gameProperties.setRareItemChance(10);

        itemService = new ItemService(characterRepository, clanRepository, weaponRepository, weaponDetailsRepository,
                consumableRepository, consumableDetailsRepository, randomService, gameProperties);
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
        weaponDetails.setName("Knife");

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
        weaponDetails.setName("Knife");

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
        weaponDetails.setName("Knife");

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
}
