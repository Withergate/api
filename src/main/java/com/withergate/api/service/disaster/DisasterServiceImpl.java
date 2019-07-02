package com.withergate.api.service.disaster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.DisasterAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterDetails;
import com.withergate.api.model.disaster.DisasterSolution;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.repository.action.DisasterActionRepository;
import com.withergate.api.repository.disaster.DisasterDetailsRepository;
import com.withergate.api.repository.disaster.DisasterRepository;
import com.withergate.api.repository.disaster.DisasterSolutionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.CombatService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Disaster service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class DisasterServiceImpl implements DisasterService {

    // turns when disasters will be triggered
    private static final int[] DISASTER_TURNS = new int[]{15, 30, 45, 60};

    // how many turns in advance will the disaster be known
    private static final int DISASTER_VISIBILITY = 10;

    private final DisasterRepository disasterRepository;
    private final DisasterDetailsRepository disasterDetailsRepository;
    private final DisasterSolutionRepository disasterSolutionRepository;
    private final DisasterActionRepository disasterActionRepository;
    private final DisasterPenaltyService disasterPenaltyService;
    private final ClanService clanService;
    private final TurnRepository turnRepository;
    private final RandomService randomService;
    private final NotificationService notificationService;
    private final CombatService combatService;

    @Override
    public Disaster getCurrentDisaster() {
        return disasterRepository.findFirstByCompleted(false);
    }

    @Override
    public DisasterSolution getDisasterSolution(String identifier) {
        return disasterSolutionRepository.getOne(identifier);
    }

    @Override
    public Disaster getDisasterForClan(int clanId) {
        Clan clan = clanService.getClan(clanId);

        // get the current disaster
        Disaster disaster = disasterRepository.findFirstByCompleted(false);

        // get current turn
        Turn turn = turnRepository.findFirstByOrderByTurnIdDesc();

        // no disaster to show
        if (disaster == null) {
            return null;
        }

        // check if clan knows about this disaster
        boolean isVisible = false;
        if ((disaster.getTurn() - DISASTER_VISIBILITY - clan.getInformationLevel()) <= turn.getTurnId()) {
            isVisible = true;
        }

        return isVisible ? disaster : null;
    }

    @Override
    public void handleDisaster(int turnId) {
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

    @Override
    public void saveDisasterAction(DisasterAction action) {
        disasterActionRepository.save(action);
    }

    @Override
    public void processDisasterActions(int turnId) {
        for (DisasterAction action : disasterActionRepository.findAllByState(ActionState.PENDING)) {
            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());

            // handle action
            handleAction(action, notification);

            // mark action as completed
            action.setState(ActionState.COMPLETED);

            // save notification
            notificationService.save(notification);
        }
    }

    private void triggerDisaster(int turnId, Disaster disaster) {
        log.debug("Triggering disaster: {}", disaster.getDetails().getIdentifier());

        // handle disaster
        for (Clan clan : clanService.getAllClans()) {
            // handle disaster
            handleClanDisaster(turnId, clan, disaster);

            // reset disaster progress
            clan.setDisasterProgress(0);
        }

        // mark disaster as completed
        disaster.setCompleted(true);
    }


    private void prepareNextDisaster(int turnId) {
        log.debug("Preparing next disaster.");

        List<Disaster> previousDisasters = disasterRepository.findAll();
        Set<String> identifiers = new HashSet<>();
        for (Disaster disaster : previousDisasters) {
            identifiers.add(disaster.getDetails().getIdentifier());
        }

        List<DisasterDetails> detailsList = disasterDetailsRepository.findAll()
                // filter out all previous disasters
                .stream().filter(d -> !identifiers.contains(d.getIdentifier()))
                .collect(Collectors.toList());

        if (detailsList.size() == 0) {
            log.error("No new disasters found.");

            return;
        }

        // prepare disaster
        DisasterDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));

        Disaster disaster = new Disaster();
        disaster.setDetails(details);
        disaster.setCompleted(false);

        // get the lowest possible turn for the next disaster
        for (int turn : DISASTER_TURNS) {
            if (turnId < turn) {
                disaster.setTurn(turn);
                break;
            }
        }

        log.debug("Disaster {} prepared for turn {}.", disaster.getDetails().getIdentifier(), disaster.getTurn());

        // save disaster
        disasterRepository.save(disaster);
    }

    private void handleAction(DisasterAction action, ClanNotification notification) {
        log.debug("Handling disaster action {} with character {}.", action.getSolution().getIdentifier(), action.getCharacter().getId());

        notificationService.addLocalizedTexts(notification.getText(), "disaster.action", new String[]{},
                action.getSolution().getName());

        Character character = action.getCharacter();
        boolean success = false;
        switch (action.getSolution().getSolutionType()) {
            case AUTOMATIC: {
                success = true;
                break;
            }
            case CRAFTSMANSHIP: {
                int roll = randomService.getRandomInt(1, RandomServiceImpl.K6);
                int result = character.getCraftsmanship() + roll;
                notification.getDetails().add(getActionRollDetail(action.getSolution().getDifficulty(), roll, result));
                if (result >= action.getSolution().getDifficulty()) {
                    success = true;
                }
                break;
            }
            case INTELLECT: {
                int roll = randomService.getRandomInt(1, RandomServiceImpl.K6);
                int result = character.getIntellect() + roll;
                notification.getDetails().add(getActionRollDetail(action.getSolution().getDifficulty(), roll, result));
                if (result >= action.getSolution().getDifficulty()) {
                    success = true;
                }
                break;
            }
            case COMBAT: {
                success = combatService.handleSingleCombat(notification, action.getSolution().getDifficulty(), action.getCharacter());
            }
            default: log.error("Unknown solution type: {}", action.getSolution().getSolutionType());
        }

        if (success) {
            handleActionSuccess(action, notification);
        } else {
            handleActionFailure(action, notification);
        }
    }

    private void handleActionSuccess(DisasterAction action, ClanNotification notification) {
        // reward experience
        action.getCharacter().setExperience(action.getCharacter().getExperience() + 2);
        notification.setExperience(2);

        // increase clan progress
        Clan clan = action.getCharacter().getClan();
        clan.setDisasterProgress(clan.getDisasterProgress() + action.getSolution().getBonus());

        // set notification text
        notificationService.addLocalizedTexts(notification.getText(), "disaster.action.success", new String[]{});
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.disaster.action.success",
                new String[]{String.valueOf(action.getSolution().getBonus())});
        notification.getDetails().add(detail);
    }

    private void handleActionFailure(DisasterAction action, ClanNotification notification) {
        // reward experience
        action.getCharacter().setExperience(action.getCharacter().getExperience() + 1);
        notification.setExperience(1);

        // set notification text
        notificationService.addLocalizedTexts(notification.getText(), "disaster.action.failure", new String[]{});
    }

    private void handleClanDisaster(int turnId, Clan clan, Disaster disaster) {
        log.debug("Handling disaster for clan {}", clan.getId());

        ClanNotification notification = new ClanNotification(turnId, clan.getId());
        notification.setHeader(clan.getName());

        disasterPenaltyService.handleDisasterPenalties(clan, notification, disaster);
    }

    private NotificationDetail getActionRollDetail(int difficulty, int roll, int result) {
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.action.roll",
                new String[]{String.valueOf(difficulty), String.valueOf(roll), String.valueOf(result)});
        return detail;
    }

}
