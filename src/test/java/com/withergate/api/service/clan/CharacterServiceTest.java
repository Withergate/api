package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CharacterServiceTest {

    private CharacterService characterService;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private ClanNotificationRepository clanNotificationRepository;

    @Mock
    private NameService nameService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        characterService = new CharacterService(characterRepository, randomService, clanNotificationRepository,
                nameService);
    }

    @Test
    public void testGivenCharacterServiceWhenLoadingCharacterThenVerifyRepositoryCalled() {
        // given service

        // when loading character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");

        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        Character result = characterService.load(1);

        // then verify character from repository returned
        assertEquals(character, result);
    }

    @Test
    public void testGivenCharacterServiceWhenSavingCharacterThenVerifyRepositoryCalled() {
        // given service

        // when loading character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");

        characterService.save(character);

        // then verify repository called
        Mockito.verify(characterRepository).save(character);
    }

    @Test
    public void testGivenCharacterServiceWhenGeneratingCharacterThenVerifyRandomValuesUsed() {
        // given service

        // when generating random character
        Mockito.when(randomService.getRandomGender()).thenReturn(Gender.MALE);
        Mockito.when(nameService.generateRandomName(Gender.MALE)).thenReturn("Rusty Nick");
        Mockito.when(randomService.getRandomInt(1, RandomService.K10)).thenReturn(8); // hp
        Mockito.when(randomService.getRandomInt(1, 5)).thenReturn(3,3, 4, 2, 3, 4);

        Character result = characterService.generateRandomCharacter();

        // then verify correct values used
        assertEquals("Rusty Nick", result.getName());
        assertEquals(Gender.MALE, result.getGender());
        assertEquals(18, result.getHitpoints());
        assertEquals(18, result.getMaxHitpoints());
        assertEquals(3, result.getCombat());
        assertEquals(3, result.getScavenge());
        assertEquals(4, result.getCraftsmanship());
        assertEquals(2, result.getIntellect());
    }

    @Test
    public void testGivenReadyCharacterWhenPerformingEndTurnUpdatesThenVerifyCharacterHealed() {
        // given character
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setBuildings(new HashMap<>());

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(5);
        character.setMaxHitpoints(7);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);

        List<Character> characters = new ArrayList<>();
        characters.add(character);

        Mockito.when(characterRepository.findAllByState(CharacterState.READY)).thenReturn(characters);

        // when performing healing
        Mockito.when(randomService.getRandomInt(1, 2)).thenReturn(1);

        characterService.performCharacterTurnUpdates(1);

        // then verify character updated
        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);

        Mockito.verify(characterRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getId());
        assertEquals(6, captor.getValue().getHitpoints());
    }

    @Test
    public void testGivenReadyCharacterWithFullHitpointsWhenPerformingEndTurnUpdatesThenVerifyCharacterNotHealed() {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(7);
        character.setMaxHitpoints(7);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);

        List<Character> characters = new ArrayList<>();
        characters.add(character);

        Mockito.when(characterRepository.findAllByState(CharacterState.READY)).thenReturn(characters);

        // when performing healing
        characterService.performCharacterTurnUpdates(1);

        // then verify character not updated
        ArgumentCaptor<Character> captor = ArgumentCaptor.forClass(Character.class);

        Mockito.verify(characterRepository, Mockito.never()).save(captor.capture());
    }
}
