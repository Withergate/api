package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.character.TavernOffer.State;
import com.withergate.api.repository.action.TavernActionRepository;
import com.withergate.api.repository.clan.TavernOfferRepository;
import com.withergate.api.service.clan.CharacterService;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tavernService = new TavernServiceImpl(tavernActionRepository, notificationService, tavernOfferRepository,
                characterService);
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
    public void testGivenTavernActionWhenSavingActionThenVerifyRepositoryCalled() {
        // given action
        TavernAction action = new TavernAction();
        action.setCharacter(mockCharacter(1, "Joe"));
        action.setState(ActionState.PENDING);

        TavernOffer offer = new TavernOffer();
        offer.setState(State.AVAILABLE);
        offer.setCharacter(mockCharacter(2, "Julia"));
        offer.setPrice(50);
        action.setOffer(offer);

        // when saving action
        tavernService.saveTavernAction(action);

        // then verify repository called
        Mockito.verify(tavernActionRepository).save(action);
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
        tavernService.processTavernActions(1);

        // then verify character hired and action completed
        Assert.assertEquals(clan, hired.getClan());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());

        Mockito.verify(tavernOfferRepository).delete(offer);
    }

    @Test
    public void testGivenClanWhenPreparingTavernOffersThenVerifyOffersCreatedAndOldDeleted() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);

        TavernOffer offer = new TavernOffer();
        offer.setId(1);

        List<TavernOffer> offers = new ArrayList<>();
        offers.add(offer);
        Mockito.when(tavernOfferRepository.findAllByClan(clan)).thenReturn(offers);

        Character character1 = mockCharacter(1, "Joe");
        Character character2 = mockCharacter(2, "Julia");
        Character character3 = mockCharacter(3, "James");

        Mockito.when(characterService.generateRandomCharacter(Mockito.any()))
                .thenReturn(character1, character2, character3);

        // when preparing offers
        tavernService.prepareTavernOffers(clan, new CharacterFilter());

        // then verify old offers deleted and new created
        Mockito.verify(tavernOfferRepository).delete(offer);
        Mockito.verify(characterService).save(character1);
        Mockito.verify(characterService).save(character2);
        Mockito.verify(characterService).save(character3);
        Mockito.verify(tavernOfferRepository, Mockito.times(3)).save(Mockito.any(TavernOffer.class));
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
