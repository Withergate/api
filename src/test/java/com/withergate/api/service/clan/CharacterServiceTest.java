package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.TraitDetailsRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.NameServiceImpl;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    private NotificationService notificationService;

    @Mock
    private TraitDetailsRepository traitDetailsRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        characterService = new CharacterServiceImpl(characterRepository, randomService, nameService, traitDetailsRepository,
                notificationService);
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
        Mockito.when(nameService.generateRandomName(Gender.MALE)).thenReturn("Rusty Nick");
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K10)).thenReturn(8); // hp
        Mockito.when(randomService.getRandomInt(1, 5)).thenReturn(3,3, 4, 2, 3, 4);

        Character result = characterService.generateRandomCharacter(new CharacterFilter());

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
        assertEquals(6, character.getHitpoints());
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

    @Test
    public void testGivenCharactersWhenDeletingDeadThenVerifyCorrectCharactersDeleted() {
        // given characters
        List<Character> characters = new ArrayList<>();

        Clan clan = new Clan();
        clan.setId(1);
        clan.setCharacters(new HashSet<>());

        Character character1 = new Character();
        character1.setId(1);
        character1.setLevel(1);
        character1.setExperience(0);
        character1.setHitpoints(1);
        characters.add(character1);
        character1.setClan(clan);
        clan.getCharacters().add(character1);

        Character character2 = new Character();
        character2.setId(2);
        character2.setLevel(1);
        character2.setExperience(0);
        character2.setHitpoints(0);
        characters.add(character2);
        character2.setClan(clan);
        clan.getCharacters().add(character2);

        Mockito.when(characterRepository.findAll()).thenReturn(characters);

        // when deleting dead
        characterService.performCharacterTurnUpdates(1);

        // then verify correct characters deleted
        Mockito.verify(characterRepository, Mockito.never()).delete(character1);
        Mockito.verify(characterRepository).delete(character2);
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
        character.setHitpoints(10);
        character.setId(1);
        character.setLevel(1);
        character.setExperience(11);
        character.setTraits(new HashMap<>());
        character.setClan(clan);

        List<Character> characters = new ArrayList<>();
        characters.add(character);
        Mockito.when(characterRepository.findAll()).thenReturn(characters);

        // when levelling up
        TraitDetails details = new TraitDetails();
        details.setIdentifier(TraitName.BUILDER);
        List<TraitDetails> detailsList = new ArrayList<>();
        detailsList.add(details);
        Mockito.when(traitDetailsRepository.findAll()).thenReturn(detailsList);
        Mockito.when(randomService.getRandomInt(0, 1)).thenReturn(0);

        characterService.performCharacterTurnUpdates(1);

        // then verify trait assigned
        Assert.assertEquals(details, character.getTraits().values().iterator().next().getDetails());
        Assert.assertEquals(2, character.getLevel());
        Assert.assertEquals(1, character.getExperience());
    }

}
