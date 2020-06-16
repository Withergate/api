package com.withergate.api.service.trade;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ResourceTradeAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.MarketTradeRequest;
import com.withergate.api.game.model.request.PublishOfferRequest;
import com.withergate.api.game.model.request.ResourceTradeRequest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.trade.MarketOffer;
import com.withergate.api.game.model.trade.MarketOffer.State;
import com.withergate.api.game.model.trade.TradeType;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.repository.action.ResourceTradeActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.trade.MarketOfferRepository;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.service.action.ActionOrder;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.profile.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Trade service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    private static final int RESOURCE_TRADE_LIMIT = 20;
    private static final int CLANS_PER_OFFER = 5;

    private final ResourceTradeActionRepository resourceTradeActionRepository;
    private final NotificationService notificationService;
    private final ItemService itemService;
    private final MarketOfferRepository marketOfferRepository;
    private final CharacterService characterService;
    private final ClanRepository clanRepository;
    private AchievementService achievementService;

    @Autowired
    public void setAchievementService(@Lazy AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @Transactional
    @Override
    public void saveAction(ResourceTradeRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting resource trade action for request {}.", request);
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        ResourceTradeAction action = new ResourceTradeAction();
        action.setCharacter(character);
        action.setType(request.getType());
        action.setFood(request.getFood());
        action.setJunk(request.getJunk());
        action.setState(ActionState.PENDING);

        // check resource limit
        if (request.getJunk() + request.getFood() > TradeServiceImpl.RESOURCE_TRADE_LIMIT) {
            throw new InvalidActionException("Your character cannot carry that much!");
        }

        if (request.getJunk() + request.getFood() < 1) {
            throw new InvalidActionException("No resources specified!");
        }

        // check if clan has enough resources
        if (request.getType().equals(TradeType.BUY)) {
            int resourcesToBuy = request.getFood() + request.getJunk();
            int cost = resourcesToBuy * 2;
            if (clan.getCaps() < cost) {
                throw new InvalidActionException("Not enough caps!");
            }

            // pay the price and save the action
            clan.changeCaps(- cost);
        }

        if (request.getType().equals(TradeType.SELL)) {
            if (clan.getFood() < request.getFood()) {
                throw new InvalidActionException("Not enough food!");
            }
            if (clan.getJunk() < request.getJunk()) {
                throw new InvalidActionException("Not enough junk!");
            }

            // pay the price
            clan.changeFood(- request.getFood());
            clan.changeJunk(- request.getJunk());
        }

        // save the action
        resourceTradeActionRepository.save(action);

        // mark character as busy and save the clan
        character.setState(CharacterState.BUSY);
    }

    @Transactional
    @Override
    public void handleMarketTradeAction(MarketTradeRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting market trade action for request {}.", request);

        Clan clan = clanRepository.getOne(clanId);
        MarketOffer offer = loadMarketOffer(request.getOfferId());

        if (offer == null || !offer.getState().equals(State.PUBLISHED)) {
            throw new InvalidActionException("This offer is not available.");
        }

        if (offer.getSeller() != null && clan.getId() == offer.getSeller().getId()) {
            throw new InvalidActionException("You cannot buy your own item!");
        }

        if (clan.getCaps() < offer.getPrice()) {
            throw new InvalidActionException("Not enough caps to perform this action.");
        }

        // pay caps
        clan.changeCaps(- offer.getPrice());

        // change offer state
        offer.setState(State.PENDING);
        offer.setBuyer(clan);
    }

    @Override
    public MarketOffer loadMarketOffer(int offerId) {
        return marketOfferRepository.getOne(offerId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void runActions(int turn) {
        // resource trade actions
        for (ResourceTradeAction action : resourceTradeActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turn, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            // process action
            processResourceTradeAction(action, notification);

            // save notification
            notificationService.save(notification);

            // mark action as done
            action.setState(ActionState.COMPLETED);
        }

        // market trade actions
        for (MarketOffer offer : marketOfferRepository.findAllByState(State.PENDING)) {
            processMarketTradeAction(offer, turn);

            // mark offer as sold
            offer.setState(State.SOLD);
        }

        // computer trades
        performComputerTradeActions(turn);
        prepareComputerMarketOffers(turn);
    }

    @Override
    public int getOrder() {
        return ActionOrder.TRADE_ORDER;
    }

    @Transactional
    @Override
    public void publishMarketOffer(PublishOfferRequest request, int clanId) throws InvalidActionException {
        Item item = itemService.loadItem(request.getItemId());

        // check validity
        if (item == null || item.getClan().getId() != clanId) {
            throw new InvalidActionException("This item does not belong to your clan!");
        }
        if (request.getPrice() < item.getDetails().getPrice()) {
            throw new InvalidActionException("Price must not be lower than market price!");
        }
        if (request.getPrice() > item.getDetails().getPrice() * 2) {
            throw new InvalidActionException("Price must not be higher than double the market price!");
        }

        // create market offer
        MarketOffer offer = new MarketOffer();
        offer.setSeller(item.getClan());
        offer.setItem(item);
        offer.setPrice(request.getPrice());
        offer.setIntelligent(request.isIntelligent());
        offer.setState(State.PUBLISHED);
        marketOfferRepository.save(offer);

        // remove item from clan storage
        item.setClan(null);
    }

    @Transactional
    @Override
    public void deleteMarketOffer(int offerId, int clanId) throws InvalidActionException {
        Optional<MarketOffer> offer = marketOfferRepository.findById(offerId);

        if (offer.isEmpty() || offer.get().getSeller().getId() != clanId) {
            throw new InvalidActionException("This offer either does not exist or does not belong to your clan.");
        }

        if (!offer.get().getState().equals(State.PUBLISHED)) {
            throw new InvalidActionException("This offer is not in PUBLISHED state and cannot be cancelled.");
        }

        // return item to the seller
        Item item = offer.get().getItem();
        item.setClan(offer.get().getSeller());

        // delete the offer
        marketOfferRepository.delete(offer.get());
    }

    @Override
    public Page<MarketOffer> getMarketOffersByState(State state, Pageable pageable) {
        return marketOfferRepository.findAllByStateOrderByPriceAsc(state, pageable);
    }

    private void performComputerTradeActions(int turnId) {
        for (MarketOffer offer : marketOfferRepository.findAllByState(State.PUBLISHED)) {
            // intelligent offers
            if (offer.isIntelligent()) {
                offer.setPrice(offer.getItem().getDetails().getPrice());
            }

            // if item is offered for market price, sell it
            if (offer.getPrice() == offer.getItem().getDetails().getPrice()) {
                Clan clan = offer.getSeller();
                clan.changeCaps(offer.getPrice());

                ClanNotification notification = new ClanNotification(turnId, clan.getId());
                notification.setHeader(clan.getName());
                notificationService.addLocalizedTexts(notification.getText(), "clan.trade.item.computer",
                        new String[]{}, offer.getItem().getDetails().getName());
                notification.changeCaps(offer.getItem().getDetails().getPrice());
                notificationService.save(notification);

                // delete offer
                marketOfferRepository.delete(offer);
            }
        }
    }

    private void prepareComputerMarketOffers(int turn) {
        log.debug("Preparing computer market offers.");

        // delete old unprocessed offers
        for (MarketOffer offer : marketOfferRepository.findAllByState(State.PUBLISHED)) {
            if (offer.getSeller() == null) {
                itemService.deleteItem(offer.getItem());
                marketOfferRepository.delete(offer);
            }
        }

        // prepare new offers
        for (int i = 0; i <= clanRepository.count() / CLANS_PER_OFFER; i++) {
            Item item = itemService.generateRandomItem(turn);

            MarketOffer offer = new MarketOffer();
            offer.setItem(item);
            offer.setPrice(item.getDetails().getPrice() * 2);
            offer.setState(State.PUBLISHED);
            offer.setSeller(null);
            marketOfferRepository.save(offer);
        }
    }

    private void processResourceTradeAction(ResourceTradeAction action, ClanNotification notification) {
        Clan clan = action.getCharacter().getClan();

        if (action.getType().equals(TradeType.BUY)) {
            clan.changeJunk(action.getJunk());
            notification.changeJunk(action.getJunk());

            clan.changeFood(action.getFood());
            notification.changeFood(action.getFood());

            notificationService.addLocalizedTexts(notification.getText(), "character.trade.resourcesBuy", new String[]{});
        } else if (action.getType().equals(TradeType.SELL)) {
            clan.changeCaps(action.getJunk() + action.getFood());
            notification.changeCaps(action.getJunk() + action.getFood());

            notificationService.addLocalizedTexts(notification.getText(), "character.trade.resourcesSell", new String[]{});
        }
    }

    private void processMarketTradeAction(MarketOffer offer, int turnId) {
        ClanNotification buyerNotification = new ClanNotification(turnId, offer.getBuyer().getId());
        buyerNotification.setHeader(offer.getBuyer().getName());

        // transfer item
        Item item = offer.getItem();
        item.setClan(offer.getBuyer());
        buyerNotification.setItem(true);

        // update notification
        notificationService.addLocalizedTexts(buyerNotification.getText(), "clan.trade.item.bought",
                new String[]{offer.getSeller() != null ? offer.getSeller().getName() : "NPC"}, offer.getItem().getDetails().getName());
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.item.bought", new String[]{},
                offer.getItem().getDetails().getName());
        buyerNotification.getDetails().add(detail);

        // handle research bonuses
        handleBonuses(offer.getBuyer(), buyerNotification);

        notificationService.save(buyerNotification);

        if (offer.getSeller() != null) {
            Clan seller = offer.getSeller();

            ClanNotification sellerNotification = new ClanNotification(turnId, offer.getSeller().getId());
            sellerNotification.setHeader(seller.getName());

            seller.changeCaps(offer.getPrice());
            sellerNotification.changeCaps(offer.getPrice());
            notificationService.addLocalizedTexts(sellerNotification.getText(), "clan.trade.item.sold",
                    new String[]{offer.getBuyer().getName()}, offer.getItem().getDetails().getName());

            notificationService.save(sellerNotification);

            // achievement
            if (offer.getPrice() == offer.getItem().getDetails().getPrice() * 2) {
                achievementService.checkAchievementAward(seller.getId(), AchievementType.TRADE_PRICE_MAX);
            }
        }
    }

    private void handleBonuses(Clan buyer, ClanNotification buyerNotification) {
        Research research = buyer.getResearch(ResearchBonusType.TRADE_FAME);
        if (research != null && research.isCompleted() && buyer.getCaps() >= research.getDetails().getCostAction()) {
            // pay caps
            buyer.changeCaps(- research.getDetails().getCostAction());
            buyerNotification.changeCaps(- research.getDetails().getCostAction());

            // award fame
            buyer.changeFame(research.getDetails().getValue());
            buyerNotification.changeFame(research.getDetails().getValue(), buyer, research.getDetails().getIdentifier());

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), research.getDetails().getBonusText(), new String[]{});
            buyerNotification.getDetails().add(detail);
        }
    }

}
