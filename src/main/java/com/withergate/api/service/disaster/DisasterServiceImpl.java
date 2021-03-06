package com.withergate.api.service.disaster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.DisasterAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.disaster.DisasterDetails;
import com.withergate.api.game.model.disaster.DisasterSolution;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.DisasterRequest;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.repository.action.DisasterActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.disaster.DisasterDetailsRepository;
import com.withergate.api.game.repository.disaster.DisasterRepository;
import com.withergate.api.game.repository.disaster.DisasterSolutionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.action.ActionOrder;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.ConditionValidator;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.turn.TurnService;
import com.withergate.api.service.utils.ActionCostUtils;
import com.withergate.api.service.utils.BonusUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Disaster service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class DisasterServiceImpl implements DisasterService {

    private final DisasterRepository disasterRepository;
    private final DisasterDetailsRepository disasterDetailsRepository;
    private final DisasterSolutionRepository disasterSolutionRepository;
    private final DisasterActionRepository disasterActionRepository;
    private final DisasterResolutionService disasterResolutionService;
    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final TurnService turnService;
    private final RandomService randomService;
    private final NotificationService notificationService;
    private final EncounterService encounterService;
    private final ItemService itemService;
    private final GameProperties gameProperties;

    @Override
    public Disaster getDisasterForClan(int clanId) {
        Clan clan = clanRepository.getOne(clanId);

        // get the current disaster
        Disaster disaster = disasterRepository.findFirstByCompleted(false);

        // get current turn
        Turn turn = turnService.getCurrentTurn();

        // no disaster to show
        if (disaster == null) {
            return null;
        }

        // check if clan knows about this disaster
        boolean isVisible = false;
        if ((disaster.getTurn() - gameProperties.getDisasterVisibility() - clan.getInformationLevel()) <= turn.getTurnId()) {
            isVisible = true;
        }

        return isVisible ? disaster : null;
    }

    @Transactional
    @Override
    public void saveAction(DisasterRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting disaster action for request {}.", request);
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if disaster solution belongs to the current disaster
        Disaster disaster = disasterRepository.findFirstByCompleted(false);
        DisasterSolution solution = disasterSolutionRepository.getOne(request.getSolution());
        if (!solution.getDisaster().equals(disaster.getDetails())) {
            throw new InvalidActionException(null, "This solution either doesn't exist or doesn't belong to the current disaster.");
        }

        // check condition
        ConditionValidator.checkActionCondition(character, solution.getCondition(), solution.getActionCost().getItemCost());

        // check completion
        if (clan.getDisasterProgress() >= 100) {
            throw new InvalidActionException(null, "Your already averted this disaster!");
        }

        // check resources & pay
        solution.getActionCost().payResources(clan);

        // create action
        DisasterAction action = new DisasterAction();
        action.setCharacter(character);
        action.setSolution(solution);
        action.setState(ActionState.PENDING);
        disasterActionRepository.save(action);

        // mark character as busy and save the clan
        character.setState(CharacterState.BUSY);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Retryable
    @Override
    public void runActions(int turn) {
        for (DisasterAction action : disasterActionRepository.findAllByState(ActionState.PENDING)) {
            // prepare notification
            ClanNotification notification = new ClanNotification(turn, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            // handle action
            handleAction(action, notification);

            // mark action as completed
            action.setState(ActionState.COMPLETED);

            // save notification
            notificationService.save(notification);
        }

        // handle disaster
        handleDisaster(turn);
    }

    @Override
    public int getOrder() {
        return ActionOrder.DISASTER_ORDER;
    }

    private void triggerDisaster(int turnId, Disaster disaster) {
        log.debug("Triggering disaster: {}", disaster.getDetails().getIdentifier());

        // handle disaster
        for (Clan clan : clanRepository.findAll()) {
            // handle disaster
            handleClanDisaster(turnId, clan, disaster);

            // reset disaster progress
            clan.setDisasterProgress(0);
        }

        // mark disaster as completed
        disaster.setCompleted(true);
    }

    private void handleDisaster(int turnId) {
        log.debug("Checking current disaster");

        // get the current disaster
        Disaster disaster = disasterRepository.findFirstByCompleted(false);

        // check disaster
        if (disaster != null) {
            // check trigger
            if (disaster.getTurn() > turnId) {
                log.debug("Existing disaster planned for turn {}. {} turns left.", disaster.getTurn(), disaster.getTurn() - turnId);
            } else {
                triggerDisaster(turnId, disaster);
                prepareNextDisaster(turnId);
            }
        } else {
            prepareNextDisaster(turnId);
        }
    }

    private void prepareNextDisaster(int turnId) {
        log.debug("Preparing next disaster.");

        // check end game
        if (turnId >= gameProperties.getDisasterTurns().get(gameProperties.getDisasterTurns().size() - 1)) {
            return;
        }

        Disaster disaster = new Disaster();
        disaster.setCompleted(false);

        // get the lowest possible turn for the next disaster
        List<Integer> disasterTurns = gameProperties.getDisasterTurns();
        boolean finalDisaster = false; // are we preparing final disaster?
        for (int i = 0; i < disasterTurns.size(); i++) {
            int turn = disasterTurns.get(i);
            if (turnId < turn) {
                disaster.setTurn(turn);

                // check final turn
                if (i == disasterTurns.size() - 1) finalDisaster = true;
                break;
            }
        }

        // filter out all previous disasters
        List<Disaster> previousDisasters = disasterRepository.findAll();
        Set<String> identifiers = new HashSet<>();
        for (Disaster previous : previousDisasters) {
            identifiers.add(previous.getDetails().getIdentifier());
        }

        // find disaster details
        List<DisasterDetails> detailsList = disasterDetailsRepository.findAllByFinalDisaster(finalDisaster)
                // filter out all previous disasters
                .stream().filter(d -> !identifiers.contains(d.getIdentifier()))
                .collect(Collectors.toList());

        if (detailsList.isEmpty()) {
            log.error("No new disasters found.");

            return;
        }

        // prepare disaster
        DisasterDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));
        disaster.setDetails(details);

        log.debug("Disaster {} prepared for turn {}.", disaster.getDetails().getIdentifier(), disaster.getTurn());

        // save disaster
        disasterRepository.save(disaster);
    }

    private void handleAction(DisasterAction action, ClanNotification notification) {
        log.debug("Handling disaster action {} with character {}.", action.getSolution().getIdentifier(), action.getCharacter().getId());

        notificationService.addLocalizedTexts(notification.getText(), "disaster.action", new String[]{},
                action.getSolution().getName());

        // pay cost
        ActionCostUtils.handlePostActionPayment(action.getSolution().getActionCost(), action.getCharacter(), notification,
                randomService, itemService);

        Character character = action.getCharacter();
        boolean success = encounterService.handleSolution(character, action.getSolution().getSolutionType(),
                action.getSolution().getDifficulty(), notification);

        if (success) {
            handleActionSuccess(action, notification);
        } else {
            handleActionFailure(action, notification);
        }
    }

    private void handleActionSuccess(DisasterAction action, ClanNotification notification) {
        // reward experience
        action.getCharacter().changeExperience(2);
        notification.changeExperience(2);

        int progress = action.getSolution().getBonus();
        progress += BonusUtils.getBonus(action.getCharacter(), BonusType.DISASTER, notification, notificationService);

        // increase clan progress
        Clan clan = action.getCharacter().getClan();
        clan.changeDisasterProgress(progress);
        if (clan.getDisasterProgress() > 100) {
            // 100% is maximumG
            clan.setDisasterProgress(100);
        }

        // set notification text
        notificationService.addLocalizedTexts(notification.getText(), "disaster.action.success", new String[]{});
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.action.success",
                new String[]{String.valueOf(progress)});
        notification.getDetails().add(detail);
    }

    private void handleActionFailure(DisasterAction action, ClanNotification notification) {
        // reward experience
        action.getCharacter().changeExperience(1);
        notification.changeExperience(1);

        // set notification text
        notificationService.addLocalizedTexts(notification.getText(), "disaster.action.failure", new String[]{});
    }

    private void handleClanDisaster(int turnId, Clan clan, Disaster disaster) {
        log.debug("Handling disaster for clan {}", clan.getId());

        ClanNotification notification = new ClanNotification(turnId, clan.getId());
        notification.setHeader(clan.getName());
        notification.setImageUrl(disaster.getDetails().getImageUrl());

        disasterResolutionService.handleDisasterResolution(clan, notification, disaster);

        // save notification
        notificationService.save(notification);
    }

}
