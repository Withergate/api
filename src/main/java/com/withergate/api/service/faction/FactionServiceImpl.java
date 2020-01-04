package com.withergate.api.service.faction;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionDescriptor;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.FactionAction;
import com.withergate.api.model.action.FactionAction.Type;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.faction.Faction;
import com.withergate.api.model.faction.FactionAid;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.FactionRequest;
import com.withergate.api.repository.action.FactionActionRepository;
import com.withergate.api.repository.faction.FactionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Faction service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FactionServiceImpl implements FactionService {

    private final FactionRepository factionRepository;
    private final CharacterService characterService;
    private final FactionActionRepository factionActionRepository;
    private final NotificationService notificationService;
    private final RandomService randomService;
    private final GameProperties properties;

    @Transactional
    @Override
    public void saveFactionAction(FactionRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting faction action for request {}.", request);
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);

        FactionAction action = new FactionAction();
        action.setType(request.getType());
        action.setState(ActionState.PENDING);
        action.setCharacter(character);

        if (request.getType().equals(Type.JOIN)) {
            if (character.getClan().getFaction() != null) {
                throw new InvalidActionException("This clan already belongs to a faction.");
            }
            if (character.getClan().getFame() < properties.getFactionEntryFame()) {
                throw new InvalidActionException("Not enough fame to join this faction.");
            }
            for (Character ch : character.getClan().getCharacters()) {
                if (ch.getCurrentAction().isPresent()
                        && ch.getCurrentAction().get().getDescriptor().equals(ActionDescriptor.FACTION.name())) {
                    throw new InvalidActionException("You are already trying to join a faction.");
                }
            }

            // check faction exists
            Faction faction = factionRepository.getOne(request.getFaction());
            action.setFaction(faction.getIdentifier());
        }

        Clan clan = character.getClan();
        if (request.getType().equals(Type.SUPPORT)) {
            if (clan.getFaction() == null) {
                throw new InvalidActionException("Clan must be in a faction to perform this action.");
            }

            FactionAid aid = getFactionAid(request.getFactionAid(), character.getClan().getFaction());
            // check cost
            if (clan.getCaps() < aid.getCapsCost() || clan.getFood() < aid.getFoodCost()
                    || clan.getJunk() < aid.getJunkCost()) {
                throw new InvalidActionException("Not enough resources to perform this action");
            }

            // pay resources
            clan.changeCaps(-aid.getCapsCost());
            clan.changeFood(-aid.getFoodCost());
            clan.changeJunk(-aid.getJunkCost());

            action.setFactionAid(aid);
        }

        // mark character as busy
        character.setState(CharacterState.BUSY);

        // save action
        factionActionRepository.save(action);
    }

    @Override
    public List<Faction> getFactions() {
        return factionRepository.findAll();
    }

    @Override
    public void processFactionActions(int turnId) {
        log.debug("Processing faction actions...");

        for (FactionAction action : factionActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            Character character = action.getCharacter();

            switch (action.getType()) {
                case JOIN:
                    Faction faction = factionRepository.getOne(action.getFaction());
                    joinFaction(faction, character, notification);
                    break;
                case SUPPORT:
                    handleSupportAction(action, character, notification);
                default:
                    log.error("Unknown action type: {}.", action.getType());
            }

            // save notification
            notificationService.save(notification);

            // mark action processed
            action.setState(ActionState.COMPLETED);
        }
    }

    private void joinFaction(Faction faction, Character character, ClanNotification notification) {
        Clan clan = character.getClan();

        clan.setFaction(faction);

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), "faction.join", new String[]{}, faction.getName());
    }

    private void handleSupportAction(FactionAction action, Character character, ClanNotification notification) {
        FactionAid aid = action.getFactionAid();

        notificationService.addLocalizedTexts(notification.getText(), "faction.aid", new String[]{},
                character.getClan().getFaction().getName());

        switch (aid.getAidType()) {
            case SUPPORT:
                // no effect
                break;
            case SACRIFICE:
                int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);
                character.changeHitpoints(-injury);
                notification.changeInjury(injury);
                if (character.getHitpoints() < 1) {
                    notification.setDeath(true);
                }
                break;
            default:
                log.error("Unknown aid type: {}", aid.getAidType());
        }

        character.getClan().changeFactionPoints(aid.getFactionPoints());
        notification.changeFactionPoints(aid.getFactionPoints());
        character.getClan().changeFame(aid.getFame());
        notification.changeFame(aid.getFame());

        // increase factions points
        Faction faction = character.getClan().getFaction();
        faction.setPoints(faction.getPoints() + aid.getFactionPoints());
    }

    private FactionAid getFactionAid(int aidId, Faction faction) throws InvalidActionException {
        for (FactionAid aid : faction.getFactionAids()) {
            if (aid.getId() == aidId) {
                return aid;
            }
        }
        throw new InvalidActionException("This action was not found.");
    }
}
