package com.withergate.api.service.trade;

import java.util.Optional;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.request.PublishOfferRequest;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails.ResearchName;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.model.trade.TradeType;
import com.withergate.api.repository.action.ResourceTradeActionRepository;
import com.withergate.api.repository.trade.MarketOfferRepository;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Trade service implementation.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    public static final int RESOURCE_TRADE_LIMIT = 20;

    private final ResourceTradeActionRepository resourceTradeActionRepository;
    private final NotificationService notificationService;
    private final ItemService itemService;
    private final MarketOfferRepository marketOfferRepository;

    @Override
    public void saveResourceTradeAction(ResourceTradeAction action) {
        resourceTradeActionRepository.save(action);
    }

    @Override
    public MarketOffer loadMarketOffer(int offerId) {
        return marketOfferRepository.getOne(offerId);
    }

    @Override
    public void processResourceTradeActions(int turnId) {
        for (ResourceTradeAction action : resourceTradeActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            // process action
            processResourceTradeAction(action, notification);

            // save notification
            notificationService.save(notification);

            // mark action as done
            action.setState(ActionState.COMPLETED);
        }
    }

    @Override
    public void processMarketTradeActions(int turnId) {
        for (MarketOffer offer : marketOfferRepository.findAllByState(State.PENDING)) {
            ClanNotification buyerNotification = new ClanNotification(turnId, offer.getBuyer().getId());
            buyerNotification.setHeader(offer.getBuyer().getName());

            ClanNotification sellerNotification = new ClanNotification(turnId, offer.getSeller().getId());
            sellerNotification.setHeader(offer.getSeller().getName());

            processMarketTradeAction(offer, buyerNotification, sellerNotification);

            // save notification
            notificationService.save(buyerNotification);
            notificationService.save(sellerNotification);

            // mark offer as sold
            offer.setState(State.SOLD);
        }
    }

    @Transactional
    @Override
    public void publishMarketOffer(PublishOfferRequest request, int clanId) throws InvalidActionException {
        Item item = itemService.loadItemByType(request.getItemId(), request.getType());
        ItemDetails details = itemService.loadItemDetailsByType(request.getItemId(), request.getType());

        // check validity
        if (item == null || item.getClan().getId() != clanId) {
            throw new InvalidActionException("This item does not belong to your clan!");
        }
        if (request.getPrice() < details.getPrice()) {
            throw new InvalidActionException("Price is too low!");
        }

        // create market offer
        MarketOffer offer = new MarketOffer();
        offer.setDetails(details);
        offer.setSeller(item.getClan());
        offer.setItemId(request.getItemId());
        offer.setPrice(request.getPrice());
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
        Item item = itemService.loadItemByType(offer.get().getItemId(), offer.get().getDetails().getItemType());
        item.setClan(offer.get().getSeller());

        // delete the offer
        marketOfferRepository.delete(offer.get());
    }

    @Override
    public Page<MarketOffer> getMarketOffersByState(State state, Pageable pageable) {
        return marketOfferRepository.findAllByState(state, pageable);
    }

    @Override
    public void performComputerTradeActions(int turnId) {
        for (MarketOffer offer : marketOfferRepository.findAllByState(State.PUBLISHED)) {
            // if item is offered for market price, sell it
            if (offer.getPrice() == offer.getDetails().getPrice()) {
                Clan clan = offer.getSeller();
                clan.changeCaps(offer.getPrice());

                ClanNotification notification = new ClanNotification(turnId, clan.getId());
                notification.setHeader(clan.getName());
                notificationService.addLocalizedTexts(notification.getText(), "clan.trade.item.computer",
                        new String[]{}, offer.getDetails().getName());
                notification.changeCaps(offer.getDetails().getPrice());
                notificationService.save(notification);

                // delete offer
                marketOfferRepository.delete(offer);
            }
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

    private void processMarketTradeAction(MarketOffer offer, ClanNotification buyerNotification,
                                          ClanNotification sellerNotification) {
        // add money to seller
        int price = offer.getPrice();
        Clan seller = offer.getSeller();
        seller.changeCaps(price);
        sellerNotification.changeCaps(price);
        notificationService.addLocalizedTexts(sellerNotification.getText(), "clan.trade.item.sold",
                new String[]{offer.getBuyer().getName()}, offer.getDetails().getName());

        // transfer item
        Item item = itemService.loadItemByType(offer.getItemId(), offer.getDetails().getItemType());
        item.setClan(offer.getBuyer());

        // update notification
        notificationService.addLocalizedTexts(buyerNotification.getText(), "clan.trade.item.bought",
                new String[]{seller.getName()}, offer.getDetails().getName());
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.item.bought", new String[]{}, offer.getDetails().getName());
        buyerNotification.getDetails().add(detail);

        // handle research bonuses
        handleBonuses(offer.getBuyer(), buyerNotification);
    }

    private void handleBonuses(Clan buyer, ClanNotification buyerNotification) {
        Research collecting = buyer.getResearch().get(ResearchName.COLLECTING);
        if (collecting != null && collecting.isCompleted() && buyer.getCaps() >= 10) {
            // pay caps
            buyer.changeCaps(- 10);
            buyerNotification.changeCaps(- 10);

            // award fame
            buyer.changeFame(collecting.getDetails().getValue());
            buyerNotification.changeFame(collecting.getDetails().getValue());

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.research.collecting", new String[]{});
            buyerNotification.getDetails().add(detail);
        }
    }
}
