package com.withergate.api.service.trade;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.MarketTradeAction;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.GearDetails;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.OutfitDetails;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.request.PublishOfferRequest;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.model.trade.TradeType;
import com.withergate.api.repository.action.MarketTradeActionRepository;
import com.withergate.api.repository.action.ResourceTradeActionRepository;
import com.withergate.api.repository.trade.MarketOfferRepository;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TradeServiceTest {

    private TradeServiceImpl tradeService;

    @Mock
    private ResourceTradeActionRepository resourceTradeActionRepository;

    @Mock
    private MarketTradeActionRepository marketTradeActionRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ItemService itemService;

    @Mock
    private MarketOfferRepository marketOfferRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tradeService = new TradeServiceImpl(resourceTradeActionRepository, marketTradeActionRepository, notificationService,
                itemService, marketOfferRepository);
    }

    @Test
    public void testGivenResourceTradeActionWhenSavingActionThenVerifyActionSaved() {
        // given action
        ResourceTradeAction action = new ResourceTradeAction();
        action.setCharacter(new Character());
        action.setFood(10);
        action.setType(TradeType.BUY);
        action.setState(ActionState.PENDING);

        // when saving action
        tradeService.saveResourceTradeAction(action);

        // then verify action saved
        Mockito.verify(resourceTradeActionRepository).save(action);
    }

    @Test
    public void testGivenMarketTradeActionWhenSavingActionThenVerifyActionSaved() {
        // given action
        MarketTradeAction action = new MarketTradeAction();
        action.setOffer(new MarketOffer());
        action.setCharacter(new Character());
        action.setState(ActionState.PENDING);

        // when saving action
        tradeService.saveMarketTradeAction(action);

        // then verify action saved
        Mockito.verify(marketTradeActionRepository).save(action);
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
    public void testGivenStateWhenLoadingMarketOffersThenVerifyOffesrLoaded() {
        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setPrice(20);
        offer.setState(State.PUBLISHED);

        List<MarketOffer> offers = new ArrayList<>();
        offers.add(offer);
        Mockito.when(marketOfferRepository.findAllByState(State.PUBLISHED)).thenReturn(offers);

        // given state when loading offers
        List<MarketOffer> result = tradeService.getMarketOffersByState(State.PUBLISHED);

        // then verify offers loaded
        Assert.assertEquals(offers, result);
    }

    @Test
    public void givenItemWhenPublishingOfferThenVerifyOfferSaved() throws Exception {
        // given item
        Clan clan = new Clan();
        clan.setId(2);

        OutfitDetails details = new OutfitDetails();
        details.setArmor(1);
        details.setIdentifier("RAGS");
        details.setPrice(20);
        Outfit outfit = new Outfit();
        outfit.setId(1);
        outfit.setDetails(details);
        outfit.setClan(clan);

        Mockito.when(itemService.loadItemByType(1, ItemType.OUTFIT)).thenReturn(outfit);
        Mockito.when(itemService.loadItemDetailsByType(1, ItemType.OUTFIT)).thenReturn(details);

        PublishOfferRequest request = new PublishOfferRequest();
        request.setItemId(1);
        request.setPrice(20);
        request.setType(ItemType.OUTFIT);

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

        OutfitDetails details = new OutfitDetails();
        details.setArmor(1);
        details.setIdentifier("RAGS");
        details.setPrice(20);
        Outfit outfit = new Outfit();
        outfit.setId(1);
        outfit.setDetails(details);
        outfit.setClan(clan);

        Mockito.when(itemService.loadItemByType(1, ItemType.OUTFIT)).thenReturn(outfit);
        Mockito.when(itemService.loadItemDetailsByType(1, ItemType.OUTFIT)).thenReturn(details);

        PublishOfferRequest request = new PublishOfferRequest();
        request.setItemId(1);
        request.setPrice(20);
        request.setType(ItemType.OUTFIT);

        // when publishing offer
        tradeService.publishMarketOffer(request, 2);
    }

    @Test(expected = InvalidActionException.class)
    public void givenItemWhenPublishingOfferWithInvalidPriceThenExpectException() throws Exception {
        // given item
        Clan clan = new Clan();
        clan.setId(2);

        OutfitDetails details = new OutfitDetails();
        details.setArmor(1);
        details.setIdentifier("RAGS");
        details.setPrice(20);
        Outfit outfit = new Outfit();
        outfit.setId(1);
        outfit.setDetails(details);
        outfit.setClan(clan);

        Mockito.when(itemService.loadItemByType(1, ItemType.OUTFIT)).thenReturn(outfit);
        Mockito.when(itemService.loadItemDetailsByType(1, ItemType.OUTFIT)).thenReturn(details);

        PublishOfferRequest request = new PublishOfferRequest();
        request.setItemId(1);
        request.setPrice(10);
        request.setType(ItemType.OUTFIT);

        // when publishing offer
        tradeService.publishMarketOffer(request, 2);
    }

    @Test
    public void givenMarketOfferWhenDeletingOfferThenVerifyOfferDeletedAndItemReturned() throws Exception {
        // given market offer
        Gear gear = new Gear();
        gear.setClan(null);
        gear.setId(2);
        GearDetails details = new GearDetails();
        details.setItemType(ItemType.GEAR);
        gear.setDetails(details);

        Mockito.when(itemService.loadItemByType(2, ItemType.GEAR)).thenReturn(gear);

        Clan clan = new Clan();
        clan.setId(3);

        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setState(State.PUBLISHED);
        offer.setSeller(clan);
        offer.setItemId(2);
        offer.setDetails(details);

        Mockito.when(marketOfferRepository.getOne(1)).thenReturn(offer);

        // when deleting offer
        tradeService.deleteMarketOffer(1, 3);

        // then verify offer deleted and item returned
        Mockito.verify(marketOfferRepository).delete(offer);
        Assert.assertEquals(clan, gear.getClan());
    }

    @Test(expected = InvalidActionException.class)
    public void givenMarketOfferFromDifferentClanWhenDeletingOfferThenExpectException() throws Exception {
        // given market offer
        Gear gear = new Gear();
        gear.setClan(null);
        gear.setId(2);
        GearDetails details = new GearDetails();
        details.setItemType(ItemType.GEAR);
        gear.setDetails(details);

        Mockito.when(itemService.loadItemByType(2, ItemType.GEAR)).thenReturn(gear);

        Clan clan = new Clan();
        clan.setId(4);

        MarketOffer offer = new MarketOffer();
        offer.setId(1);
        offer.setState(State.PUBLISHED);
        offer.setSeller(clan);
        offer.setItemId(2);
        offer.setDetails(details);

        Mockito.when(marketOfferRepository.getOne(1)).thenReturn(offer);

        // when deleting offer
        tradeService.deleteMarketOffer(1, 3);
    }

    @Test
    public void givenMarketOfferWhenProcessingMarketOfferActionThenVerifyOfferHandled() {
        // given market offer
        Weapon weapon = new Weapon();
        weapon.setId(1);
        weapon.setClan(null);
        WeaponDetails details = new WeaponDetails();
        details.setItemType(ItemType.WEAPON);
        weapon.setDetails(details);
        Mockito.when(itemService.loadItemByType(1, ItemType.WEAPON)).thenReturn(weapon);

        Clan seller = new Clan();
        seller.setId(3);
        seller.setCaps(10);

        Clan buyer = new Clan();
        buyer.setId(4);

        MarketOffer offer = new MarketOffer();
        offer.setId(2);
        offer.setItemId(1);
        offer.setDetails(details);
        offer.setState(State.SOLD);
        offer.setPrice(20);
        offer.setSeller(seller);

        Character character = new Character();
        character.setClan(buyer);

        MarketTradeAction action = new MarketTradeAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setOffer(offer);
        Mockito.when(marketTradeActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing trade actions
        tradeService.processMarketTradeActions(1);

        // then verify offer handled
        Assert.assertEquals(buyer, weapon.getClan());
        Assert.assertEquals(30, seller.getCaps());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Mockito.verify(marketOfferRepository).delete(offer);
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
        Weapon weapon = new Weapon();
        weapon.setId(1);
        weapon.setClan(null);
        WeaponDetails details = new WeaponDetails();
        details.setItemType(ItemType.WEAPON);
        details.setPrice(20);
        weapon.setDetails(details);
        Mockito.when(itemService.loadItemByType(1, ItemType.WEAPON)).thenReturn(weapon);

        Clan seller = new Clan();
        seller.setId(3);
        seller.setCaps(10);

        MarketOffer offer = new MarketOffer();
        offer.setId(2);
        offer.setItemId(1);
        offer.setDetails(details);
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
