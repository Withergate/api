package com.withergate.api.service.location;

import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.type.AttributeTemplate.Type;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionDescriptor;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.TavernAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterFilter;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.character.TavernOffer.State;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.TavernRequest;
import com.withergate.api.game.repository.action.TavernActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.clan.TavernOfferRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanServiceImpl;
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
    private static final int FAME_PRICE = 15;
    private static final int CHARACTER_LIMIT = 5;
    private static final int DEFAULT_ATTRIBUTES = 11;

    private final TavernActionRepository tavernActionRepository;
    private final NotificationService notificationService;
    private final TavernOfferRepository tavernOfferRepository;
    private final CharacterService characterService;
    private final ClanRepository clanRepository;
    private final RandomService randomService;
    private final GameProperties gameProperties;

    @Override
    public TavernOffer loadTavernOffer(int offerId) {
        return tavernOfferRepository.getOne(offerId);
    }

    @Override
    public List<TavernOffer> loadTavernOffers(TavernOffer.State state, Clan clan) {
        return tavernOfferRepository.findAllByStateAndClan(state, clan);
    }

    @Transactional
    @Override
    public void saveTavernAction(TavernRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting tavern action request: {}", request.toString());
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        TavernOffer offer = loadTavernOffer(request.getOfferId());
        if (offer == null || offer.getClan().getId() != clanId
                || !offer.getState().equals(TavernOffer.State.AVAILABLE)) {
            throw new InvalidActionException("This offer either doesn't exists or does not belong to your clan!");
        }

        int cost = offer.getPrice();
        if (clan.getCaps() < cost) {
            throw new InvalidActionException("Not enough resources to perform this action!");
        }

        int limit = clan.getPopulationLimit();
        for (Character ch : clan.getCharacters()) {
            if (ch.getCurrentAction().isPresent() && ch.getCurrentAction().get().getDescriptor().equals(ActionDescriptor.TAVERN.name())) {
                limit--;
            }
        }
        if (clan.getCharacters().size() >= limit) {
            throw new InvalidActionException("Population limit exceeded.");
        }

        clan.changeCaps(- cost);
        clan.changeFame(- offer.getFame());

        TavernAction action = new TavernAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setOffer(offer);

        tavernActionRepository.save(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);

        // mark offer as hired
        offer.setState(TavernOffer.State.HIRED);
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
            int sum = ClanServiceImpl.MAX_CHARACTER_STRENGTH - i * 4;
            Character character = characterService.generateRandomCharacter(filter,
                    randomService.getRandomAttributeCombination(sum, Type.RANDOM));
            characterService.save(character);
            int price = calculateOfferPrice(character);

            TavernOffer offer = new TavernOffer();
            offer.setState(TavernOffer.State.AVAILABLE);
            offer.setCharacter(character);
            offer.setClan(clan);
            offer.setPrice(price);
            offer.setFame(0);

            tavernOfferRepository.save(offer);

            // update filter
            filter.getAvatars().add(character.getImageUrl());
            filter.getNames().add(character.getName());
        }

        // special offer for players with less characters
        if (clan.getCharacters().stream().filter(ch -> ch.getHitpoints() > 0).count() < CHARACTER_LIMIT) {
            Character character = characterService.generateRandomCharacter(filter,
                    randomService.getRandomAttributeCombination(DEFAULT_ATTRIBUTES, Type.RANDOM));
            characterService.save(character);

            TavernOffer offer = new TavernOffer();
            offer.setState(TavernOffer.State.AVAILABLE);
            offer.setCharacter(character);
            offer.setClan(clan);
            offer.setPrice(FAME_PRICE);
            offer.setFame(FAME_PRICE);

            tavernOfferRepository.save(offer);
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
