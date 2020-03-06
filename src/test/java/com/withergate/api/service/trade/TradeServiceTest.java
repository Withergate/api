package com.withergate.api.service.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ResourceTradeAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.request.PublishOfferRequest;
import com.withergate.api.game.model.trade.MarketOffer;
import com.withergate.api.game.model.trade.MarketOffer.State;
import com.withergate.api.game.model.trade.TradeType;
import com.withergate.api.game.repository.action.ResourceTradeActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.trade.MarketOfferRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.profile.AchievementService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static com.withergate.api.game.model.trade.MarketOffer.State.SOLD;

public class TradeServiceTest {

    private TradeServiceImpl tradeService;

    @Mock
    private ResourceTradeActionRepository resourceTradeActionRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ItemService itemService;

    @Mock
    private MarketOfferRepository marketOfferRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private AchievementService achievementService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tradeService = new TradeServiceImpl(resourceTradeActionRepository, notificationService,
                itemService, marketOfferRepository, characterService, clanRepository);
        tradeService.setAchievementService(achievementService);
    }

    @Test
    public void testGivenOfferIdWhenLoadingMarketOfferThenVerifyOfferLoaded() {
        // given offer ID
        int offerId = 1;

        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setPrice(20);

        Mockito.when(marketOfferRepository.getOne(1)).thenReturn(offer);

        // when loading offer
        MarketOffer result = tradeService.loadMarketOffer(offerId);

        // then verify offer loaded
        Assert.assertEquals(offer, result);
    }

    @Test
    public void testGivenStateWhenLoadingMarketOffersThenVerifyOffersLoaded() {
        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setPrice(20);
        offer.setState(State.PUBLISHED);

        List<MarketOffer> offers = new ArrayList<>();
        offers.add(offer);
        Mockito.when(marketOfferRepository.findAllByStateOrderByPriceAsc(Mockito.eq(State.PUBLISHED), Mockito.any()))
                .thenReturn(new PageImpl<>(offers));

        // given state when loading offers
        Page<MarketOffer> result = tradeService.getMarketOffersByState(State.PUBLISHED, PageRequest.of(0, 20));

        // then verify offers loaded
        Assert.assertEquals(offers, result.getContent());
    }

    @Test
    public void givenItemWhenPublishingOfferThenVerifyOfferSaved() throws Exception {
        // given item
        Clan clan = new Clan();
        clan.setId(2);

        ItemDetails details = new ItemDetails();
        details.setBonus(1);
        details.setIdentifier("RAGS");
        details.setPrice(20);
        Item outfit = new Item();
        outfit.setId(1);
        outfit.setDetails(details);
        outfit.setClan(clan);

        Mockito.when(itemService.loadItem(1)).thenReturn(outfit);

        PublishOfferRequest request = new PublishOfferRequest();
        request.setItemId(1);
        request.setPrice(20);

        // when publishing offer
        tradeService.publishMarketOffer(request, 2);

        // then verify offer saved
        ArgumentCaptor<MarketOffer> captor = ArgumentCaptor.forClass(MarketOffer.class);
        Mockito.verify(marketOfferRepository).save(captor.capture());
        Assert.assertEquals(clan, captor.getValue().getSeller());
        Assert.assertEquals(20, captor.getValue().getPrice());
        Assert.assertEquals(State.PUBLISHED, captor.getValue().getState());
    }

    @Test(expected = InvalidActionException.class)
    public void givenItemFromDifferentClanWhenPublishingOfferThenExpectException() throws Exception {
        // given item
        Clan clan = new Clan();
        clan.setId(3);

        ItemDetails details = new ItemDetails();
        details.setBonus(1);
        details.setIdentifier("RAGS");
        details.setPrice(20);
        Item outfit = new Item();
        outfit.setId(1);
        outfit.setDetails(details);
        outfit.setClan(clan);

        Mockito.when(itemService.loadItem(1)).thenReturn(outfit);

        PublishOfferRequest request = new PublishOfferRequest();
        request.setItemId(1);
        request.setPrice(20);

        // when publishing offer
        tradeService.publishMarketOffer(request, 2);
    }

    @Test(expected = InvalidActionException.class)
    public void givenItemWhenPublishingOfferWithInvalidPriceThenExpectException() throws Exception {
        // given item
        Clan clan = new Clan();
        clan.setId(2);

        ItemDetails details = new ItemDetails();
        details.setBonus(1);
        details.setIdentifier("RAGS");
        details.setPrice(20);
        Item outfit = new Item();
        outfit.setId(1);
        outfit.setDetails(details);
        outfit.setClan(clan);

        Mockito.when(itemService.loadItem(1)).thenReturn(outfit);

        PublishOfferRequest request = new PublishOfferRequest();
        request.setItemId(1);
        request.setPrice(10);

        // when publishing offer
        tradeService.publishMarketOffer(request, 2);
    }

    @Test
    public void givenMarketOfferWhenDeletingOfferThenVerifyOfferDeletedAndItemReturned() throws Exception {
        // given market offer
        Item gear = new Item();
        gear.setClan(null);
        gear.setId(2);
        ItemDetails details = new ItemDetails();
        details.setItemType(ItemType.GEAR);
        gear.setDetails(details);

        Mockito.when(itemService.loadItem(2)).thenReturn(gear);

        Clan clan = new Clan();
        clan.setId(3);

        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setState(State.PUBLISHED);
        offer.setSeller(clan);
        offer.setItem(gear);

        Mockito.when(marketOfferRepository.findById(1)).thenReturn(Optional.of(offer));

        // when deleting offer
        tradeService.deleteMarketOffer(1, 3);

        // then verify offer deleted and item returned
        Mockito.verify(marketOfferRepository).delete(offer);
        Assert.assertEquals(clan, gear.getClan());
    }

    @Test(expected = InvalidActionException.class)
    public void givenMarketOfferFromDifferentClanWhenDeletingOfferThenExpectException() throws Exception {
        // given market offer
        Item gear = new Item();
        gear.setClan(null);
        gear.setId(2);
        ItemDetails details = new ItemDetails();
        details.setItemType(ItemType.GEAR);
        gear.setDetails(details);

        Mockito.when(itemService.loadItem(2)).thenReturn(gear);

        Clan clan = new Clan();
        clan.setId(4);

        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setState(State.PUBLISHED);
        offer.setSeller(clan);
        offer.setItem(gear);

        Mockito.when(marketOfferRepository.getOne(1)).thenReturn(offer);

        // when deleting offer
        tradeService.deleteMarketOffer(1, 3);
    }

    @Test
    public void givenMarketOfferWhenProcessingTradeThenVerifyOfferHandled() {
        // given market offer
        Item weapon = new Item();
        weapon.setId(1);
        weapon.setClan(null);
        ItemDetails details = new ItemDetails();
        details.setItemType(ItemType.WEAPON);
        weapon.setDetails(details);
        Mockito.when(itemService.loadItem(1)).thenReturn(weapon);

        Clan seller = new Clan();
        seller.setId(3);
        seller.setCaps(10);

        Clan buyer = new Clan();
        buyer.setId(4);

        MarketOffer offer = new MarketOffer();
        offer.setId(2);
        offer.setItem(weapon);
        offer.setState(State.PENDING);
        offer.setPrice(20);
        offer.setSeller(seller);
        offer.setBuyer(buyer);

        Mockito.when(marketOfferRepository.findAllByState(State.PENDING)).thenReturn(List.of(offer));

        // when processing trade
        tradeService.processMarketTradeActions(1);

        // then verify offer handled
        Assert.assertEquals(buyer, weapon.getClan());
        Assert.assertEquals(30, seller.getCaps());
        Assert.assertEquals(SOLD, offer.getState());
    }

    @Test
    public void testGivenResourceActionWhenBuyingThenVerifyTradeHandled() {
        // given resource trade action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(50);
        clan.setFood(0);
        clan.setJunk(0);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);

        ResourceTradeAction action = new ResourceTradeAction();
        action.setType(TradeType.BUY);
        action.setFood(5);
        action.setJunk(10);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        Mockito.when(resourceTradeActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        tradeService.processResourceTradeActions(1);

        // then verify action handled
        Assert.assertEquals(5, clan.getFood());
        Assert.assertEquals(10, clan.getJunk());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenResourceActionWhenSellingThenVerifyTradeHandled() {
        // given resource trade action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(50);
        clan.setFood(5);
        clan.setJunk(5);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);

        ResourceTradeAction action = new ResourceTradeAction();
        action.setType(TradeType.SELL);
        action.setFood(5);
        action.setJunk(5);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        Mockito.when(resourceTradeActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        tradeService.processResourceTradeActions(1);

        // then verify action handled
        Assert.assertEquals(60, clan.getCaps());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void givenMarketOfferWhenPriceIsMarketPriceThenVerifyItemPurchasedByComputer() {
        // given market offer
        Item weapon = new Item();
        weapon.setId(1);
        weapon.setClan(null);
        ItemDetails details = new ItemDetails();
        details.setItemType(ItemType.WEAPON);
        details.setPrice(20);
        weapon.setDetails(details);
        Mockito.when(itemService.loadItem(1)).thenReturn(weapon);

        Clan seller = new Clan();
        seller.setId(3);
        seller.setCaps(10);

        MarketOffer offer = new MarketOffer();
        offer.setId(2);
        offer.setItem(weapon);
        offer.setState(State.PUBLISHED);
        offer.setPrice(20);
        offer.setSeller(seller);

        Mockito.when(marketOfferRepository.findAllByState(State.PUBLISHED)).thenReturn(List.of(offer));

        // when processing computer trade actions
        tradeService.performComputerTradeActions(1);

        // then verify offer handled
        Assert.assertEquals(30, seller.getCaps());
        Mockito.verify(marketOfferRepository).delete(offer);
    }

}
