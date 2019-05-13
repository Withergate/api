package com.withergate.api.service.trade;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.MarketTradeAction;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.request.PublishOfferRequest;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.model.trade.TradeType;
import com.withergate.api.repository.action.MarketTradeActionRepository;
import com.withergate.api.repository.action.ResourceTradeActionRepository;
import com.withergate.api.repository.trade.MarketOfferRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Trade service implementaion.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    public static final int RESOURCE_TRADE_LIMIT = 20;

    private final ResourceTradeActionRepository resourceTradeActionRepository;
    private final MarketTradeActionRepository marketTradeActionRepository;
    private final NotificationService notificationService;
    private final ItemService itemService;
    private final MarketOfferRepository marketOfferRepository;

    @Override
    public void saveResourceTradeAction(ResourceTradeAction action) {
        resourceTradeActionRepository.save(action);
    }

    @Override
    public void saveMarketTradeAction(MarketTradeAction action) {
        marketTradeActionRepository.save(action);
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
        for (MarketTradeAction action : marketTradeActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification buyerNotification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            buyerNotification.setHeader(action.getCharacter().getName());

            ClanNotification sellerNotification = new ClanNotification(turnId, action.getOffer().getSeller().getId());
            sellerNotification.setHeader(action.getOffer().getSeller().getName());

            processMarketTradeAction(action, buyerNotification, sellerNotification);

            // save notification
            notificationService.save(buyerNotification);
            notificationService.save(sellerNotification);

            // mark action as done
            action.setState(ActionState.COMPLETED);
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
        MarketOffer offer = marketOfferRepository.getOne(offerId);

        if (offer == null || offer.getSeller().getId() != clanId) {
            throw new InvalidActionException("This offer either does not exist or does not belong to your clan.");
        }

        // return item to the seller
        Item item = itemService.loadItemByType(offer.getItemId(), offer.getDetails().getItemType());
        item.setClan(offer.getSeller());

        // delete the offer
        marketOfferRepository.delete(offer);
    }

    @Override
    public List<MarketOffer> getMarketOffersByState(State state) {
        return marketOfferRepository.findAllByState(state);
    }

    private void processResourceTradeAction(ResourceTradeAction action, ClanNotification notification) {
        Clan clan = action.getCharacter().getClan();

        if (action.getType().equals(TradeType.BUY)) {
            clan.setJunk(clan.getJunk() + action.getJunk());
            notification.setJunkIncome(action.getJunk());

            clan.setFood(clan.getFood() + action.getFood());
            notification.setFoodIncome(action.getFood());

            notificationService.addLocalizedTexts(notification.getText(), "character.trade.resourcesBuy", new String[]{});
        } else if (action.getType().equals(TradeType.SELL)) {
            clan.setCaps(clan.getCaps() + action.getJunk() + action.getFood());
            notification.setCapsIncome(action.getJunk() + action.getFood());

            notificationService.addLocalizedTexts(notification.getText(), "character.trade.resourcesSell", new String[]{});
        }
    }

    private void processMarketTradeAction(MarketTradeAction action, ClanNotification buyerNotification,
                                          ClanNotification sellerNotification) {
        MarketOffer offer = action.getOffer();

        // add money to seller
        int price = offer.getPrice();
        Clan seller = offer.getSeller();
        seller.setCaps(seller.getCaps() + price);
        sellerNotification.setCapsIncome(price);
        notificationService.addLocalizedTexts(sellerNotification.getText(), "clan.trade.item",
                new String[]{action.getCharacter().getName(), action.getCharacter().getClan().getName()});

        // transfer item
        Item item = itemService.loadItemByType(offer.getItemId(), offer.getDetails().getItemType());
        item.setClan(action.getCharacter().getClan());

        // update notification
        notificationService.addLocalizedTexts(buyerNotification.getText(), "character.trade.item", new String[]{seller.getName()});
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.item.bought", new String[]{}, offer.getDetails().getName());
        buyerNotification.getDetails().add(detail);

        // delete offer
        marketOfferRepository.delete(offer);
    }
}
