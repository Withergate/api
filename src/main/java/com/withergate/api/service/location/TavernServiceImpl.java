package com.withergate.api.service.location;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.action.TavernActionRepository;
import com.withergate.api.repository.clan.TavernOfferRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.notification.NotificationService;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Tavern service implementation.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@Slf4j
@Service
public class TavernServiceImpl implements TavernService {

    private static final int TAVERN_OFFERS = 3;
    private static final int ATTRIBUTE_PRICE = 5;
    private static final int TRAIT_PRICE = 20;

    private final TavernActionRepository tavernActionRepository;
    private final NotificationService notificationService;
    private final TavernOfferRepository tavernOfferRepository;
    private final CharacterService characterService;

    @Override
    public TavernOffer loadTavernOffer(int offerId) {
        return tavernOfferRepository.getOne(offerId);
    }

    @Override
    public List<TavernOffer> loadTavernOffers(TavernOffer.State state, Clan clan) {
        return tavernOfferRepository.findAllByStateAndClan(state, clan);
    }

    @Override
    public void saveTavernAction(TavernAction action) {
        tavernActionRepository.save(action);
    }

    @Override
    public void processTavernActions(int turnId) {
        log.debug("Processing tavern actions.");

        for (TavernAction action : tavernActionRepository.findAllByState(ActionState.PENDING)) {
            Character character = action.getCharacter();

            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
            notification.setHeader(character.getName());

            // hire character
            TavernOffer offer = action.getOffer();
            Character hired = offer.getCharacter();
            hired.setClan(character.getClan());

            notificationService.addLocalizedTexts(notification.getText(), "location.tavern.hired", new String[] {hired.getName()});
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.character.joined", new String[] {hired.getName()});
            notification.getDetails().add(detail);

            // save notification
            notificationService.save(notification);

            // mark action as completed
            action.setState(ActionState.COMPLETED);

            // delete processed offer
            tavernOfferRepository.delete(offer);
        }
    }

    @Override
    public void prepareTavernOffers(Clan clan, CharacterFilter filter) {
        log.debug("Preparing tavern offers for clan {}.", clan.getId());

        // delete all old offers
        List<TavernOffer> offers = tavernOfferRepository.findAllByClan(clan);
        for (TavernOffer offer : offers) {
            tavernOfferRepository.delete(offer);
        }

        // create new offers
        for (int i = 0; i < TAVERN_OFFERS; i++) {
            Character character = characterService.generateRandomCharacter(filter);
            characterService.save(character);
            int price = calculateOfferPrice(character);

            TavernOffer offer = new TavernOffer();
            offer.setState(TavernOffer.State.AVAILABLE);
            offer.setCharacter(character);
            offer.setClan(clan);
            offer.setPrice(price);

            tavernOfferRepository.save(offer);
        }
    }

    private int calculateOfferPrice(Character character) {
        int price = 0;

        price += character.getCombat() * ATTRIBUTE_PRICE;
        price += character.getScavenge() * ATTRIBUTE_PRICE;
        price += character.getCraftsmanship() * ATTRIBUTE_PRICE;
        price += character.getIntellect() * ATTRIBUTE_PRICE;

        if (character.getTraits().size() > 0) {
            price += TRAIT_PRICE;
        }

        return price;
    }
}
