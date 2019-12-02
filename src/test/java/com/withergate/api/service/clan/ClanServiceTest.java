package com.withergate.api.service.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.character.TavernOffer.State;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.research.ResearchService;
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

    @Mock
    private ResearchService researchService;

    @Mock
    private TavernService tavernService;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setHealing(2);
        properties.setFoodConsumption(2);
        properties.setStarvationInjury(2);
        properties.setStarvationFame(1);

        clanService = new ClanServiceImpl(clanRepository, characterService, notificationService, questService,
                buildingService, researchService, tavernService, randomService, properties);
    }

    @Test(expected = EntityConflictException.class)
    public void testGivenExistingClanNameWhenCreatingClanThenExpectException() throws Exception {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findOneByName("Stalkers")).thenReturn(clan);

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Stalkers");
        clanService.createClan(2, clanRequest, 1);

        // then expect exception
    }

    @Test(expected = EntityConflictException.class)
    public void testGivenExistingClanIdWhenCreatingClanThenExpectException() throws Exception {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findById(1)).thenReturn(Optional.of(clan));

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Dragons");
        clanService.createClan(1, clanRequest, 1);

        // then expect exception
    }

    @Test(expected = ValidationException.class)
    public void testGivenClanRequestWhenNameTooShortThenExpectException() throws Exception {
        // given request
        ClanRequest clanRequest = new ClanRequest("aaa");

        // when clan name too short
        clanService.createClan(1, clanRequest, 1);

        // then expect exception
    }

    @Test
    public void testGivenUniqueClanWhenCreatingClanThenVerifyClanSaved() throws Exception {
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

        Mockito.when(characterService.generateRandomCharacter(Mockito.any(CharacterFilter.class), Mockito.any()))
                .thenReturn(characters[0], characters[1], characters[2], characters[3], characters[4]);

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Dragons");
        clanService.createClan(2, clanRequest, 1);

        // then verify clan saved
        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanRepository).save(captor.capture());

        assertEquals("Dragons", captor.getValue().getName());
        assertEquals(5, captor.getValue().getCharacters().size());
        assertEquals(0, captor.getValue().getFame());
    }

    @Test
    public void testGivenClanRequestWhenCreatingClanLateThenVerifyBonusResourcesGiven() throws Exception {
        // given clan request
        ClanRequest clanRequest = new ClanRequest("Dragons");
        int turn = 3;

        Character[] characters = new Character[5];
        for (int i = 0; i < 5; i++) {
            Character character = new Character();
            character.setId(i);
            characters[i] = character;
        }

        Mockito.when(characterService.generateRandomCharacter(Mockito.any(CharacterFilter.class), Mockito.any()))
                .thenReturn(characters[0], characters[1], characters[2], characters[3], characters[4]);

        // when creating new clan in late turn
        clanService.createClan(2, clanRequest, turn);

        // then verify clan saved
        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanRepository).save(captor.capture());

        assertEquals("Dragons", captor.getValue().getName());
        assertEquals(26, captor.getValue().getFood());
        assertEquals(26, captor.getValue().getJunk());
        assertEquals(56, captor.getValue().getCaps());
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
    public void testGivenClanListWhenClearingArenaFlagsThenVerifyClansUnmarked() {
        // given clans
        List<Clan> clans = new ArrayList<>();
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        clan.setArena(true);
        clans.add(clan);
        clan.setCharacters(new HashSet<>());

        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when clearing arena flags
        clanService.performClanTurnUpdates(2);

        // then verify clan unmarked
        assertEquals(false, clan.isArena());
    }

    @Test
    public void testGivenClanWhenIncreasingInformationLevelThenVerifyQuestServiceCalled() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        clan.setInformationLevel(0);
        clan.setInformation(12);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when increasing information level
        clanService.performClanTurnUpdates(1);

        // then verify quest service called
        Mockito.verify(questService).assignQuests(Mockito.eq(clan), Mockito.any(ClanNotification.class));
        Assert.assertEquals(1, clan.getInformationLevel());
        Assert.assertEquals(2, clan.getInformation());
    }

    @Test
    public void testGivenClanWhenPerformingTurnActionsThenVerifyFoodConsumedAndBuildingsTriggered() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);

        Character character1 = new Character();
        character1.setId(1);
        character1.setName("John");
        character1.setClan(clan);
        character1.setHitpoints(1);
        clan.getCharacters().add(character1);

        Character character2 = new Character();
        character2.setId(2);
        character2.setName("Jane");
        character2.setHitpoints(1);
        character2.setClan(clan);
        clan.getCharacters().add(character2);

        // Ascetic
        Character character3 = new Character();
        character3.setId(3);
        character3.setName("Josh");
        character3.setClan(clan);
        character3.setHitpoints(1);
        TraitDetails details = new TraitDetails();
        details.setIdentifier(TraitName.ASCETIC);
        details.setBonus(1);
        Trait trait = new Trait();
        trait.setDetails(details);
        trait.setActive(true);
        character3.getTraits().put(TraitName.ASCETIC, trait);
        clan.getCharacters().add(character3);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when performing turn updates
        clanService.performClanTurnUpdates(1);

        // then verify food consumed and buildings triggered
        Assert.assertEquals(5, clan.getFood());
        Mockito.verify(buildingService).processPassiveBuildingBonuses(1, clan);
    }

    @Test
    public void testGivenClanWithoutFoodWhenPerformingTurnActionsThenVerifyCharacterStarving() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(0);
        clan.setFame(2);
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

        // then verify character starving and fame lost
        Assert.assertEquals(3, character.getHitpoints());
        Assert.assertEquals(1, clan.getFame());
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

    @Test
    public void testGivenDefaultActionRequestWhenChangingActionThenVerifyActionChanged() {
        // given request
        DefaultActionRequest request = new DefaultActionRequest();
        request.setDefaultAction(Clan.DefaultAction.EXPLORE_NEIGHBORHOOD);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setDefaultAction(Clan.DefaultAction.REST);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        // when changing action
        clanService.changeDefaultAction(request, 1);

        // then verify action changed
        Assert.assertEquals(Clan.DefaultAction.EXPLORE_NEIGHBORHOOD, clan.getDefaultAction());
    }

    @Test
    public void testGivenClanOffersWhenGettingOffersThenVerifyTavernServiceCalled() {
        // given offers
        Clan clan = new Clan();
        clan.setId(1);

        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        List<TavernOffer> offers = new ArrayList<>();
        TavernOffer offer = new TavernOffer();
        offer.setId(1);
        offer.setState(State.AVAILABLE);
        offer.setPrice(50);
        offer.setCharacter(new Character());
        offer.setClan(clan);
        offers.add(offer);

        Mockito.when(tavernService.loadTavernOffers(State.AVAILABLE, clan)).thenReturn(offers);

        // when getting offers
        List<TavernOffer> result = clanService.loadTavernOffers(1);

        // then verify offers returned
        Assert.assertEquals(offers, result);
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
        clan.getCharacters().add(character);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when performing healing
        clanService.performClanTurnUpdates(1);

        // then verify character updated
        assertEquals(7, character.getHitpoints());
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

        // when performing healing
        clanService.performClanTurnUpdates(1);

        // then verify character not updated
        assertEquals(7, character.getHitpoints());
    }

    @Test
    public void testGivenCharactersWhenDeletingDeadThenVerifyCorrectCharactersDeleted() {
        // given characters
        List<Character> characters = new ArrayList<>();

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setCharacters(new HashSet<>());

        Character character1 = new Character();
        character1.setId(1);
        character1.setName("John");
        character1.setLevel(1);
        character1.setExperience(0);
        character1.setHitpoints(10);
        characters.add(character1);
        character1.setClan(clan);
        clan.getCharacters().add(character1);

        Character character2 = new Character();
        character2.setId(2);
        character2.setName("Jane");
        character2.setLevel(1);
        character2.setExperience(0);
        character2.setHitpoints(0);
        characters.add(character2);
        character2.setClan(clan);
        clan.getCharacters().add(character2);

        List<Clan> clans = new ArrayList<>();
        clans.add(clan);
        Mockito.when(clanRepository.findAll()).thenReturn(clans);

        // when deleting dead
        clanService.performClanTurnUpdates(1);

        // then verify correct characters deleted
        Mockito.verify(characterService, Mockito.never()).delete(character1);
        Mockito.verify(characterService).delete(character2);
    }

}
