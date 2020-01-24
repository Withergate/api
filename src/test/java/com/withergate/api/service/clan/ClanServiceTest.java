package com.withergate.api.service.clan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.character.TavernOffer.State;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.statistics.ClanTurnStatisticsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
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
    private BuildingService buildingService;

    @Mock
    private ResearchService researchService;

    @Mock
    private TavernService tavernService;

    @Mock
    private ClanTurnService clanTurnService;

    @Mock
    private RandomService randomService;

    @Mock
    private ClanTurnStatisticsRepository statisticsRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        clanService = new ClanServiceImpl(clanRepository, characterService, buildingService, researchService, tavernService,
                clanTurnService, randomService, statisticsRepository);
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

}
