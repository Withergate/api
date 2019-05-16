package com.withergate.api.service.clan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ClanServiceTest {

    private ClanServiceImpl clanService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private QuestService questService;

    @Mock
    private BuildingService buildingService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties gameProperties = new GameProperties();
        gameProperties.setInitialClanSize(5);

        clanService = new ClanServiceImpl(clanRepository, characterService, gameProperties, notificationService, questService,
                buildingService);
    }

    @Test(expected = EntityConflictException.class)
    public void testGivenExistingClanNameWhenCreatingClanThenExpectException() throws EntityConflictException {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findOneByName("Stalkers")).thenReturn(clan);

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Stalkers");
        clanService.createClan(2, clanRequest);

        // then expect exception
    }

    @Test(expected = EntityConflictException.class)
    public void testGivenExistingClanIdWhenCreatingClanThenExpectException() throws EntityConflictException {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findById(1)).thenReturn(Optional.of(clan));

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Dragons");
        clanService.createClan(1, clanRequest);

        // then expect exception
    }

    @Test
    public void testGivenUniqueClanWhenCreatingClanThenVerifyClanSaved() throws EntityConflictException {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findById(1)).thenReturn(Optional.of(clan));

        Character[] characters = new Character[5];
        for (int i = 0; i < 5; i++) {
            Character character = new Character();
            character.setId(i);
            characters[i] = character;
        }

        Mockito.when(characterService.generateRandomCharacter())
                .thenReturn(characters[0], characters[1], characters[2], characters[3], characters[4]);

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Dragons");
        clanService.createClan(2, clanRequest);

        // then verify clan saved
        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanRepository).save(captor.capture());

        assertEquals("Dragons", captor.getValue().getName());
        assertEquals(5, captor.getValue().getCharacters().size());
        assertEquals(0, captor.getValue().getFame());
    }

    @Test
    public void testGivenClanIdWhenGettingClanThenVerifyCorrectClanRetrieved() {
        // given clan ID
        int clanId = 1;

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        Mockito.when(clanRepository.findById(1)).thenReturn(Optional.of(clan));

        // when getting clan
        Clan result = clanService.getClan(clanId);

        // then verify correct clan returned
        assertEquals(clan, result);
    }

    @Test
    public void testGivenServiceWhenGettingAllClansThenVerifyListRetrieved() {
        // given service when getting all clans

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        List<Clan> clanList = new ArrayList<>();

        Mockito.when(clanRepository.findAll()).thenReturn(clanList);

        List<Clan> result = clanService.getAllClans();

        // then verify correct list returned
        assertEquals(clanList, result);
    }

    @Test
    public void testGivenClanWhenSavingClanThenVerifyClanSaved() {
        // given clan

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        // when saving a clan
        clanService.saveClan(clan);

        // then verify clan saved
        Mockito.verify(clanRepository).save(clan);
    }

    @Test
    public void testGivenClanWhenHiringCharacterThenVerifyClanSavedWithCharacter() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        clan.setCharacters(new HashSet<>());

        Character hired = new Character();
        hired.setName("Hired");
        Mockito.when(characterService.generateRandomCharacter()).thenReturn(hired);

        // when hiring character
        clanService.hireCharacter(clan);

        // then verify clan saved with character
        assertEquals(hired, clan.getCharacters().iterator().next());
    }

    @Test
    public void testGivenClanListWhenClearingArenaFlagsThenVerifyClansUnmarked() {
        // given clans
        List<Clan> clans = new ArrayList<>();
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        clan.setArena(true);
        clans.add(clan);

        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when clearing arena flags
        clanService.clearArenaCharacters();

        // then verify clan unmarked
        assertEquals(false, clan.isArena());
    }

    @Test
    public void testGivenClanWhenIncreasingInformationLevelThenVerifyQuestServiceCalled() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        // when increasing information level
        ClanNotification notification = new ClanNotification();
        clanService.increaseInformationLevel(clan, notification, 1);

        // then verify quest service called
        Mockito.verify(questService).assignQuests(clan, notification, 1);
    }

    @Test
    public void testGivenClanWhenPerformingTurnActionsThenVerifyFoodConsumedAndBuildingsTriggered() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setCharacters(new HashSet<>());

        Character character1 = new Character();
        character1.setId(1);
        clan.getCharacters().add(character1);
        Character character2 = new Character();
        character2.setId(2);
        clan.getCharacters().add(character2);

        // Ascetic
        Character character3 = new Character();
        character3.setId(3);
        TraitDetails details = new TraitDetails();
        details.setIdentifier(TraitName.ASCETIC);
        Trait trait = new Trait();
        trait.setDetails(details);
        character3.getTraits().put(TraitName.ASCETIC, trait);
        clan.getCharacters().add(character3);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when performing turn updates
        clanService.performClanTurnUpdates(1);

        // then verify food consumed and buildings triggered
        Assert.assertEquals(8, clan.getFood());
        Mockito.verify(buildingService).processPassiveBuildingBonuses(1, clan);
    }

    @Test
    public void testGivenClanWithoutFoodWhenPerformingTurnActionsThenVerifyCharacterStarving() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(0);
        clan.setCharacters(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(5);
        clan.getCharacters().add(character);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when performing turn updates
        clanService.performClanTurnUpdates(1);

        // then verify character starving
        Assert.assertEquals(4, character.getHitpoints());
    }

    @Test
    public void testGivenClanWithoutFoodAndInjuredCharacterWhenPerformingTurnActionsThenVerifyCharacterDied() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(0);
        clan.setCharacters(new HashSet<>());

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(1);
        clan.getCharacters().add(character);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when performing turn updates
        clanService.performClanTurnUpdates(1);

        // then verify character died of starvation
        Assert.assertEquals(0, clan.getCharacters().size());
        Mockito.verify(characterService).delete(character);
    }

}
