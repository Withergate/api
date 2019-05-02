package com.withergate.api.service.trade;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResourceTradeAction;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.TradeType;
import com.withergate.api.repository.action.ResourceTradeActionRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.notification.NotificationService;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Trade service implementaion.
 *
 * @author Martin Myslik
 */
@Service
public class TradeServiceImpl implements TradeService {

    public static final int RESOURCE_TRADE_LIMIT = 20;

    private final ResourceTradeActionRepository resourceTradeActionRepository;
    private final ClanService clanService;
    private final NotificationService notificationService;

    /**
     * Constructor.
     */
    public TradeServiceImpl(
            ResourceTradeActionRepository resourceTradeActionRepository,
            ClanService clanService, NotificationService notificationService) {
        this.resourceTradeActionRepository = resourceTradeActionRepository;
        this.clanService = clanService;
        this.notificationService = notificationService;
    }

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

    @Override
    public List<MarketOffer> prepareClanMarketOffers(int clanId) {
        Clan clan = clanService.getClan(clanId);

        List<MarketOffer> marketOffers = new ArrayList<>();

        for (Weapon weapon : clan.getWeapons()) {
            marketOffers.add(new MarketOffer(weapon));
        }

        for (Outfit outfit : clan.getOutfits()) {
            marketOffers.add(new MarketOffer(outfit));
        }

        for (Gear gear : clan.getGear()) {
            marketOffers.add(new MarketOffer(gear));
        }

        for (Consumable consumable : clan.getConsumables()) {
            marketOffers.add(new MarketOffer(consumable));
        }

        return marketOffers;
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
