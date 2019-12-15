package com.withergate.api.service.quest;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.quest.QuestDetails;
import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.repository.action.QuestActionRepository;
import com.withergate.api.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Quest service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class QuestServiceImpl implements QuestService {

    private final QuestDetailsRepository questDetailsRepository;
    private final NotificationService notificationService;
    private final QuestActionRepository questActionRepository;
    private final CombatService combatService;
    private final RandomService randomService;
    private final CharacterService characterService;

    @Override
    public void assignQuests(Clan clan, ClanNotification notification) {
        log.debug("Assigning new quests to the clan.");

        Set<String> identifiers = new HashSet<>();
        clan.getQuests().forEach(quest -> identifiers.add(quest.getDetails().getIdentifier()));

        // load details and filter existing/completed quests
        List<QuestDetails> questDetails = questDetailsRepository.findAll()
                .stream()
                .filter(details -> !identifiers.contains(details.getIdentifier()))
                .collect(Collectors.toList());

        if (questDetails.isEmpty()) {
            log.error("No quests found for clan {}.", clan.getId());
            return;
        }

        // get random quest
        QuestDetails details = questDetails.get(randomService.getRandomInt(0, questDetails.size() - 1));

        Quest quest = new Quest();
        quest.setClan(clan);
        quest.setCompleted(false);
        quest.setProgress(0);
        quest.setDetails(details);

        clan.getQuests().add(quest);

        // add notification
        NotificationDetail notificationDetail = new NotificationDetail();
        notificationService.addLocalizedTexts(notificationDetail.getText(), "detail.quest.assigned", new String[]{}, details.getName());
        notification.getDetails().add(notificationDetail);
    }

    @Transactional
    @Override
    public void saveQuestAction(QuestRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting quest action for request {}.", request);
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // check if the clan contains this quest
        Quest quest = null;
        for (Quest q : clan.getQuests()) {
            if (q.getId() == request.getQuestId()) {
                // check if not completed
                if (q.isCompleted()) {
                    throw new InvalidActionException("This quest has already been completed.");
                }
                quest = q;
                break;
            }
        }

        if (quest == null) {
            throw new InvalidActionException("This quest does not exist.");
        }

        // check condition
        checkQuestCondition(character, quest);

        // persist the action
        QuestAction action = new QuestAction();
        action.setCharacter(character);
        action.setQuest(quest);
        action.setState(ActionState.PENDING);
        questActionRepository.save(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
    }

    @Override
    public void processQuestActions(int turnId) {
        log.debug("Processing quest actions.");

        for (QuestAction action : questActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            // process single action
            processQuestAction(action, notification);

            notificationService.save(notification);

            // mark action as completed
            action.setState(ActionState.COMPLETED);
        }
    }

    private void processQuestAction(QuestAction action, ClanNotification notification) {
        Character character = action.getCharacter();
        Quest quest = action.getQuest();

        boolean success = false;
        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K6);
        switch (action.getQuest().getDetails().getType()) {
            case COMBAT:
                success = combatService.handleSingleCombat(notification, quest.getDetails().getDifficulty(), character);
                break;
            case INTELLECT:
                int result = character.getIntellect() + diceRoll;
                if (result >= quest.getDetails().getDifficulty()) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(quest.getDetails().getDifficulty(), diceRoll, result));
                break;
            case CRAFTSMANSHIP:
                result = character.getCraftsmanship() + diceRoll;
                if (result >= quest.getDetails().getDifficulty()) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(quest.getDetails().getDifficulty(), diceRoll, result));
                break;
            case SCAVENGE:
                result = character.getScavenge() + diceRoll;
                if (result >= quest.getDetails().getDifficulty()) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(quest.getDetails().getDifficulty(), diceRoll, result));
                break;
            default:
                log.warn("Unknown quest type: {}", action.getQuest().getDetails().getType());
        }

        if (success) {
            handleQuestSuccess(quest, character, notification);
        } else {
            handleQuestFailure(quest, character, notification);
        }
    }

    private void handleQuestSuccess(Quest quest, Character character, ClanNotification notification) {
        // update notification
        notificationService.addLocalizedTexts(notification.getText(), "character.quest.success", new String[]{},
                quest.getDetails().getName());

        // award experience
        character.setExperience(character.getExperience() + 2);
        notification.changeExperience(2);

        quest.setProgress(quest.getProgress() + 1);

        Clan clan = quest.getClan();
        // quest has been completed
        if (quest.getProgress() >= quest.getDetails().getCompletion() && !quest.isCompleted()) {
            log.debug("Quest {} has been completed.", quest.getId());

            ClanNotification completionNotification = new ClanNotification(notification.getTurnId(), notification.getClanId());
            completionNotification.setHeader(quest.getClan().getName());
            notificationService.addLocalizedTexts(completionNotification.getText(), "quest.completed", new String[]{},
                    quest.getDetails().getName());

            quest.setCompleted(true);

            // income
            clan.changeFame(quest.getDetails().getFameReward());
            completionNotification.changeFame(quest.getDetails().getFameReward());
            clan.changeCaps(quest.getDetails().getCapsReward());
            completionNotification.changeCaps(quest.getDetails().getCapsReward());

            notificationService.save(completionNotification);
        }
    }

    private void handleQuestFailure(Quest quest, Character character, ClanNotification notification) {
        // update notification
        notificationService.addLocalizedTexts(notification.getText(), "character.quest.failure", new String[]{},
                quest.getDetails().getName());

        // award experience
        character.setExperience(character.getExperience() + 1);
        notification.changeExperience(1);
    }

    private NotificationDetail getActionRollDetail(int difficulty, int roll, int result) {
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.action.roll",
                new String[]{String.valueOf(difficulty), String.valueOf(roll), String.valueOf(result)});
        return detail;
    }

    /**
     * Checks the quest condition and throws an exception if it is not met.
     */
    private void checkQuestCondition(Character character, Quest quest) throws InvalidActionException {
        QuestDetails.Condition condition = quest.getDetails().getCondition();
        if (condition == null) {
            return;
        }

        switch (condition) {
            case FEMALE_CHARACTER:
                if (!character.getGender().equals(Gender.FEMALE)) {
                    throw new InvalidActionException("Character must be FEMALE to perform this action.");
                }
                break;
            case HEALTHY_CHARACTER:
                if (character.getHitpoints() < character.getMaxHitpoints()) {
                    throw new InvalidActionException("Character must be healthy to perform this action.");
                }
                break;
            default:
                log.error("Unknown quest condition: {}.", condition);
        }


    }

}
