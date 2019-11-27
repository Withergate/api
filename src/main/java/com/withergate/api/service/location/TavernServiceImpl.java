package com.withergate.api.service.location;

import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.character.TavernOffer.State;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.action.TavernActionRepository;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.clan.TavernOfferRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final int ATTRIBUTE_PRICE = 8;
    private static final int TRAIT_PRICE = 16;

    private final TavernActionRepository tavernActionRepository;
    private final NotificationService notificationService;
    private final TavernOfferRepository tavernOfferRepository;
    private final CharacterService characterService;
    private final ClanRepository clanRepository;
    private final GameProperties gameProperties;

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
            notification.setImageUrl(character.getImageUrl());

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

            // mark offer as processed
            offer.setState(TavernOffer.State.PROCESSED);
        }
    }

    @Override
    public void prepareTavernOffers(Clan clan) {
        log.debug("Preparing tavern offers for clan {}.", clan.getId());

        // delete all old offers
        List<TavernOffer> offers = tavernOfferRepository.findAllByClan(clan);
        for (TavernOffer offer : offers) {
            if (offer.getState().equals(State.AVAILABLE)) {
                tavernOfferRepository.delete(offer);
            }
        }

        // generate filter
        CharacterFilter filter = getCharacterFilter(clan);

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

            // update filter
            filter.getAvatars().add(character.getImageUrl());
            filter.getNames().add(character.getName());
        }
    }

    @Transactional
    @Override
    public void refreshTavernOffers(int clanId) throws InvalidActionException {
        Clan clan = clanRepository.getOne(clanId);

        // check price
        if (clan.getCaps() < gameProperties.getTavernRefreshPrice()) {
            throw new InvalidActionException("Not enough caps to perform this action.");
        }

        clan.changeCaps(- gameProperties.getTavernRefreshPrice());

        prepareTavernOffers(clan);
    }

    private int calculateOfferPrice(Character character) {
        int price = 0;

        price += character.getCombat() * ATTRIBUTE_PRICE;
        price += character.getScavenge() * ATTRIBUTE_PRICE;
        price += character.getCraftsmanship() * ATTRIBUTE_PRICE;
        price += character.getIntellect() * ATTRIBUTE_PRICE;
        price += character.getSkillPoints() * TRAIT_PRICE;

        return price;
    }

    private CharacterFilter getCharacterFilter(Clan clan) {
        CharacterFilter filter = new CharacterFilter();

        for (Character character : clan.getCharacters()) {
            filter.getNames().add(character.getName());
            filter.getAvatars().add(character.getImageUrl());
        }

        return filter;
    }
}
