package com.withergate.api.service.faction;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionDescriptor;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.FactionAction;
import com.withergate.api.game.model.action.FactionAction.Type;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.faction.ClanFactionOverview;
import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.model.faction.FactionAid;
import com.withergate.api.game.model.faction.FactionPointsOverview;
import com.withergate.api.game.model.faction.FactionsOverview;
import com.withergate.api.game.model.item.ItemDetails.Rarity;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.FactionRequest;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.repository.action.FactionActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.faction.FactionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.action.ActionOrder;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.ConditionValidator;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.utils.BonusUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Faction service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FactionServiceImpl implements FactionService {

    private static final int LEADERBOARD_SIZE = 5;

    private final FactionRepository factionRepository;
    private final CharacterService characterService;
    private final FactionActionRepository factionActionRepository;
    private final NotificationService notificationService;
    private final RandomService randomService;
    private final ClanRepository clanRepository;
    private final ItemService itemService;
    private final QuestService questService;
    private final GameProperties properties;

    @Transactional
    @Override
    public void saveAction(FactionRequest request, int clanId) throws InvalidActionException {
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
            if (aid.getCost() > 0 && clan.getCaps() < aid.getCost()) {
                throw new InvalidActionException("Not enough resources to perform this action.");
            }
            // check item cost
            ConditionValidator.checkActionCondition(character, null, aid.getItemCost());
            // check information cost
            if (aid.getInformationCost() > 0 && clan.getInformation() < aid.getInformationCost()) {
                throw new InvalidActionException("Not enough information to perform this action.");
            }
            // check influence cost
            if (aid.getFactionPointsCost() > 0 && clan.getFactionPoints() < aid.getFactionPointsCost()) {
                throw new InvalidActionException("Not enough influence to perform this action.");
            }
            // check leading condition
            if (aid.isLeading() && !clan.getFaction().getIdentifier().equals(getBestFaction().getIdentifier())) {
                throw new InvalidActionException(("Your clan must be a member of a leading faction to perform this action."));
            }

            // pay resources
            clan.changeCaps(- aid.getCost());
            clan.changeInformation(- aid.getInformationCost());
            clan.changeFactionPoints(- aid.getFactionPointsCost());

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

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void runActions(int turn) {
        for (FactionAction action : factionActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turn, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            Character character = action.getCharacter();

            switch (action.getType()) {
                case JOIN:
                    Faction faction = factionRepository.getOne(action.getFaction());
                    joinFaction(faction, character, notification);
                    break;
                case SUPPORT:
                    handleSupportAction(action, character, notification, turn);
                    break;
                default:
                    log.error("Unknown action type: {}.", action.getType());
            }

            // award experience
            character.changeExperience(1);
            notification.changeExperience(1);

            // save notification
            notificationService.save(notification);

            // mark action processed
            action.setState(ActionState.COMPLETED);
        }
    }

    @Override
    public int getOrder() {
        return ActionOrder.FACTION_ORDER;
    }

    @Override
    public FactionsOverview getOverview(int clanId) {
        Clan clan = clanRepository.getOne(clanId);
        if (clan.getFaction() == null) return null;

        List<Faction> factions = factionRepository.findAll();

        FactionsOverview overview = new FactionsOverview();
        overview.setFactions(new ArrayList<>());
        for (Faction faction : factions) {
            FactionPointsOverview fpo = new FactionPointsOverview();
            fpo.setIdentifier(faction.getIdentifier());
            fpo.setIconUrl(faction.getIconUrl());
            fpo.setName(faction.getName());
            fpo.setPoints(faction.getPoints());
            fpo.setFame(getFactionFame(faction, factions));
            overview.getFactions().add(fpo);

            // compute expected clan fame
            if (faction.getIdentifier().equals(clan.getFaction().getIdentifier())) {
                overview.setClanPoints(clan.getFactionPoints());
                overview.setClanFame(getClanFame(clan, fpo.getFame()));
                overview.setClans(getLeaderboard(faction, fpo.getFame()));
            }
        }

        return overview;
    }

    @Override
    public void handleFameDistribution(int turnId) {
        log.debug("Distributing faction fame.");

        List<Faction> factions = factionRepository.findAll();
        for (Clan clan : clanRepository.findAll()) {
            if (clan.getFaction() == null) continue;

            int fame = getClanFame(clan, getFactionFame(clan.getFaction(), factions));
            if (fame < 1) continue;
            clan.changeFame(fame);

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.setImageUrl(clan.getFaction().getImageUrl());
            notification.changeFame(fame);
            notificationService.addLocalizedTexts(notification.getText(), "faction.fame", new String[]{},
                    clan.getFaction().getName());

            // save notification
            notificationService.save(notification);
        }
    }

    @Transactional
    @Override
    public Faction getBestFaction() {
        List<Faction> factions = factionRepository.findAll().stream().sorted(Comparator.comparingInt(Faction::getPoints)
                .reversed()).collect(Collectors.toList());
        return factions.get(0);
    }

    @Override
    public Clan getBestClan(Faction faction) {
        List<ClanFactionOverview> leaderboard = getLeaderboard(faction, 0);
        if (leaderboard.isEmpty()) {
            return null;
        }

        return clanRepository.getOne(leaderboard.get(0).getClanId());
    }

    private void joinFaction(Faction faction, Character character, ClanNotification notification) {
        Clan clan = character.getClan();

        clan.setFaction(faction);
        questService.assignFactionQuests(clan, notification);

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), "faction.join", new String[]{}, faction.getName());
    }

    private void handleSupportAction(FactionAction action, Character character, ClanNotification notification, int turnId) {
        FactionAid aid = action.getFactionAid();

        notificationService.addLocalizedTexts(notification.getText(), "faction.aid", new String[]{},
                character.getClan().getFaction().getName());

        switch (aid.getAidType()) {
            case RESOURCE_SUPPORT:
                distributeResources(character.getClan(), aid, turnId, notification);
                break;
            case FACTION_SUPPORT:
                // pay cost
                if (aid.isHealthCost()) {
                    int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);
                    character.changeHitpoints(-injury);
                    notification.changeInjury(injury);
                    if (character.getHitpoints() < 1) {
                        notification.setDeath(true);
                    }
                }
                if (aid.getItemCost() != null) {
                    itemService.deleteItem(character, aid.getItemCost(), notification);
                }
                break;
            case CAPS_REWARD:
                character.getClan().changeCaps(aid.getAid());
                notification.changeCaps(aid.getAid());
                break;
            case HEALING_REWARD:
                int missingHealth = character.getMaxHitpoints() - character.getHitpoints();
                int healing = Math.min(missingHealth, aid.getAid());
                character.changeHitpoints(healing);
                notification.changeHealing(healing);
                break;
            case ITEM_REWARD:
                itemService.generateItemForCharacter(character, notification, null, Rarity.RARE, turnId);
                break;
            default:
                log.error("Unknown aid type: {}", aid.getAidType());
        }

        // increase faction points
        int factionPoints = aid.getFactionPoints();
        factionPoints += BonusUtils.getBonus(action.getCharacter(), BonusType.FACTION_POINTS, notification, notificationService);
        character.getClan().changeFactionPoints(factionPoints);
        notification.changeFactionPoints(factionPoints);

        // award fame
        character.getClan().changeFame(aid.getFame());
        notification.changeFame(aid.getFame());
    }

    private void distributeResources(Clan clan, FactionAid aid, int turnId, ClanNotification notification) {
        List<Clan> clans = clan.getFaction().getClans().stream().filter(c -> c.getFactionPoints() < clan.getFactionPoints())
                .collect(Collectors.toList());
        Collections.shuffle(clans);

        int i = 0;
        for (Clan receiver : clans) {
            if (i >= aid.getNumAid()) break;

            ClanNotification receiverNotification = new ClanNotification(turnId, receiver.getId());
            receiverNotification.setHeader(receiver.getName());
            notificationService.addLocalizedTexts(receiverNotification.getText(), "faction.aid.receive", new String[]{clan.getName()});

            receiver.changeFood(aid.getAid());
            receiverNotification.changeFood(aid.getAid());
            receiver.changeJunk(aid.getAid());
            receiverNotification.changeJunk(aid.getAid());
            receiver.changeInformation(aid.getAid());
            receiverNotification.changeInformation(aid.getAid());

            notificationService.save(receiverNotification);

            // donor detail
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.faction.aid", new String[]{receiver.getName()});
            notification.getDetails().add(detail);

            i++;
        }
    }

    private FactionAid getFactionAid(String identifier, Faction faction) throws InvalidActionException {
        for (FactionAid aid : faction.getFactionAids()) {
            if (aid.getIdentifier().equals(identifier)) {
                return aid;
            }
        }
        throw new InvalidActionException("This action was not found.");
    }

    private int getFactionFame(Faction faction, List<Faction> factions) {
        int clanCount = (int) clanRepository.countAllByFactionNotNull();
        // total fame to be redistributed
        int totalFame = properties.getFactionFameCoefficient() * properties.getFactionFameCoefficient() * clanCount;
        int totalPoints = factions.stream().mapToInt(Faction::getPoints).sum(); // total points
        double ratio = (double) faction.getPoints() / totalPoints;

        return properties.getFactionFameCoefficient() * clanCount + (int) (totalFame * ratio);
    }

    private int getClanFame(Clan clan, int factionFame) {
        double clanRatio = (double) clan.getFactionPoints() / clan.getFaction().getPoints();
        return (int) (factionFame * clanRatio);
    }

    private List<ClanFactionOverview> getLeaderboard(Faction faction, int factionFame) {
        // get faction clans sorted by faction points
        List<Clan> clans = faction.getClans().stream().sorted(Comparator.comparingInt(Clan::getFactionPoints).reversed())
                .collect(Collectors.toList());

        List<ClanFactionOverview> overviews = new ArrayList<>();
        int i = 0;
        for (Clan clan : clans) {
            if (i >= LEADERBOARD_SIZE) break;
            ClanFactionOverview overview = new ClanFactionOverview(clan.getId(), clan.getName(), clan.getFactionPoints(),
                    getClanFame(clan, factionFame));
            overviews.add(overview);
            i++;
        }

        return overviews;
    }

}
