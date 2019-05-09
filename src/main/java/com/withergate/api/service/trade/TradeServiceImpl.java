package com.withergate.api.service.trade;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.PublishOfferRequest;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import com.withergate.api.model.trade.TradeType;
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
    private final NotificationService notificationService;
    private final ItemService itemService;
    private final MarketOfferRepository marketOfferRepository;

    @Override
    public void saveResourceTradeAction(ResourceTradeAction action) {
        resourceTradeActionRepository.save(action);
    }

    @Override
    public void processResourceTradeActions(int turnId) {
        for (ResourceTradeAction action : resourceTradeActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification();
            notification.setClanId(action.getCharacter().getClan().getId());
            notification.setTurnId(turnId);
            notification.setHeader(action.getCharacter().getName());

            // process action
            processResourceTradeAction(action, notification);

            // save notification
            notificationService.save(notification);

            // mark action as done
            action.setState(ActionState.COMPLETED);
            resourceTradeActionRepository.save(action);
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
}
