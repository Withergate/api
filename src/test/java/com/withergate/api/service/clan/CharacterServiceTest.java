package com.withergate.api.service.clan;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class CharacterServiceTest {

    private CharacterServiceImpl characterService;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private NameService nameService;

    @Mock
    private TraitService traitService;

    @Mock
    private NotificationService notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        characterService = new CharacterServiceImpl(characterRepository, randomService, nameService, traitService, notificationService);
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
    public void testGivenCharacterServiceWhenLoadingAllCharactersThenVerifyRepositoryCalled() {
        // given service

        // when loading character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        List<Character> characters = new ArrayList<>();
        characters.add(character);

        Mockito.when(characterRepository.findAll()).thenReturn(characters);

        List<Character> result = characterService.loadAll();

        // then verify character from repository returned
        assertEquals(character, result.get(0));
    }

    @Test
    public void testGivenCharacterServiceWhenSavingCharacterThenVerifyRepositoryCalled() {
        // given service

        // when saving character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");

        characterService.save(character);

        // then verify repository called
        Mockito.verify(characterRepository).save(character);
    }

    @Test
    public void testGivenCharacterServiceWhenDeletingCharacterThenVerifyRepositoryCalled() {
        // given service

        // when deleting character
        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");

        characterService.delete(character);

        // then verify repository called
        Mockito.verify(characterRepository).delete(character);
    }

    @Test
    public void testGivenCharacterServiceWhenGeneratingCharacterThenVerifyRandomValuesUsed() {
        // given service

        // when generating random character
        Mockito.when(randomService.getRandomGender()).thenReturn(Gender.MALE);
        Mockito.when(nameService.generateRandomName(Mockito.eq(Gender.MALE), Mockito.any())).thenReturn("Rusty Nick");
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K10)).thenReturn(8); // hp
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6))
                .thenReturn(3,5, 1, 2, 5, 6, 1, 5);

        Character result = characterService.generateRandomCharacter(new CharacterFilter());

        // then verify correct values used
        assertEquals("Rusty Nick", result.getName());
        assertEquals(Gender.MALE, result.getGender());
        assertEquals(18, result.getHitpoints());
        assertEquals(18, result.getMaxHitpoints());
        assertEquals(4, result.getCombat());
        assertEquals(1, result.getScavenge());
        assertEquals(5, result.getCraftsmanship());
        assertEquals(3, result.getIntellect());
    }

    @Test
    public void testGivenCharacterWhenMarkingRestingThenVerifyCharacterUpdated() throws Exception {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when marking as resting
        characterService.markCharacterAsResting(1, 1);

        // then verify character updated
        Assert.assertEquals(CharacterState.RESTING, character.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBusyCharacterWhenMarkingRestingThenExpectException() throws Exception {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when marking as resting
        characterService.markCharacterAsResting(1, 1);

        // then expect exception
    }

    @Test
    public void givenCharacterWhenLevellingUpThenVerifyTraitAssigned() {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setName("John");
        character.setHitpoints(10);
        character.setId(1);
        character.setLevel(1);
        character.setExperience(11);
        character.setClan(clan);

        // when levelling up

        characterService.increaseCharacterLevel(character, 1);

        // then verify trait assigned
        Assert.assertEquals(2, character.getLevel());
        Assert.assertEquals(1, character.getExperience());
        Assert.assertEquals(1, character.getSkillPoints());
    }

}
