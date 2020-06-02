package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.TavernAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.character.TavernOffer.State;
import com.withergate.api.game.repository.action.TavernActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.clan.TavernOfferRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TavernServiceTest {

    private TavernServiceImpl tavernService;

    @Mock
    private TavernActionRepository tavernActionRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TavernOfferRepository tavernOfferRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setTavernRefreshPrice(10);

        tavernService = new TavernServiceImpl(tavernActionRepository, notificationService, tavernOfferRepository,
                characterService, clanRepository, randomService, properties);
    }

    @Test
    public void testGivenOfferIdWhenLoadingOfferThenVerifyOfferReturned() {
        // given offer ID
        int id = 1;

        TavernOffer offer = new TavernOffer();
        offer.setId(1);
        offer.setState(State.AVAILABLE);
        offer.setPrice(50);
        offer.setCharacter(mockCharacter(1, "Joe"));

        Mockito.when(tavernOfferRepository.getOne(id)).thenReturn(offer);

        // when loading tavern offer
        TavernOffer result = tavernService.loadTavernOffer(id);

        // then verify offer loaded
        Assert.assertEquals(offer, result);
    }

    @Test
    public void testGivenClanOffersWhenLoadingOffersThenVerifyRepositoryCalled() {
        // given clan offers
        Clan clan = new Clan();
        clan.setId(1);

        List<TavernOffer> offers = new ArrayList<>();
        TavernOffer offer = new TavernOffer();
        offer.setId(1);
        offer.setState(State.AVAILABLE);
        offer.setPrice(50);
        offer.setCharacter(mockCharacter(1, "Joe"));
        offers.add(offer);

        Mockito.when(tavernOfferRepository.findAllByStateAndClan(State.AVAILABLE, clan)).thenReturn(offers);

        // when loading clan offers
        List<TavernOffer> result = tavernService.loadTavernOffers(State.AVAILABLE, clan);

        // then verify correct offers returned
        Assert.assertEquals(offers, result);
    }

    @Test
    public void testGivenTavernActionWhenProcessingActionsThenVerifyCharacterHired() {
        // given action
        TavernAction action = new TavernAction();
        action.setState(ActionState.PENDING);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setCharacters(new HashSet<>());

        Character character = mockCharacter(1, "Joe");
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        clan.getCharacters().add(character);
        action.setCharacter(character);

        Character hired = mockCharacter(2, "Julia");

        TavernOffer offer = new TavernOffer();
        offer.setState(State.HIRED);
        offer.setCharacter(hired);
        offer.setPrice(50);
        offer.setClan(clan);
        action.setOffer(offer);

        List<TavernAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(tavernActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions
        tavernService.runActions(1);

        // then verify character hired and action completed
        Assert.assertEquals(clan, hired.getClan());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Assert.assertEquals(State.PROCESSED, offer.getState());
    }

    @Test
    public void testGivenClanWhenPreparingTavernOffersThenVerifyOffersCreatedAndOldMarkedAsProcessed() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);

        TavernOffer offer = new TavernOffer();
        offer.setId(1);
        offer.setState(State.AVAILABLE);

        List<TavernOffer> offers = new ArrayList<>();
        offers.add(offer);
        Mockito.when(tavernOfferRepository.findAllByClan(clan)).thenReturn(offers);

        Character character1 = mockCharacter(1, "Joe");
        Character character2 = mockCharacter(2, "Julia");
        Character character3 = mockCharacter(3, "James");
        Character character4 = mockCharacter(3, "Johny");

        Mockito.when(characterService.generateRandomCharacter(Mockito.any(), Mockito.any()))
                .thenReturn(character1, character2, character3, character4);

        // when preparing offers
        tavernService.prepareTavernOffers(clan);

        // then verify old offers deleted and new created
        Mockito.verify(tavernOfferRepository).delete(offer);
        Mockito.verify(characterService).save(character1);
        Mockito.verify(characterService).save(character2);
        Mockito.verify(characterService).save(character3);
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenClanWithoutCapsWhenRefreshingTavernOffersThenVerifyExceptionThrown() throws Exception {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(0);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        // when refreshing offers
        tavernService.refreshTavernOffers(1);

        // then verify exception thrown
    }

    private Character mockCharacter(int id, String name) {
        Character character = new Character();
        character.setId(id);
        character.setName(name);

        character.setCombat(1);
        character.setScavenge(1);
        character.setCraftsmanship(1);
        character.setIntellect(1);

        return character;
    }

}
