package com.withergate.api.service.quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.quest.QuestDetails;
import com.withergate.api.repository.action.QuestActionRepository;
import com.withergate.api.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.encounter.CombatService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        if (questDetails.size() < 1) {
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
        notificationService.addLocalizedTexts(notificationDetail.getText(), "detail.quest.assigned", new String[] {}, details.getName());
        notification.getDetails().add(notificationDetail);

    }

    @Override
    public void saveQuestAction(QuestAction action) {
        questActionRepository.save(action);
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
        notificationService.addLocalizedTexts(notification.getText(), "character.quest.success", new String[] {},
                quest.getDetails().getName());

        // award experience
        character.setExperience(character.getExperience() + 2);
        notification.setExperience(2);

        quest.setProgress(quest.getProgress() + 1);

        Clan clan = quest.getClan();
        // quest has been completed
        if (quest.getProgress() >= quest.getDetails().getCompletion() && !quest.isCompleted()) {
            log.debug("Quest {} has been completed.", quest.getId());

            ClanNotification completionNotification = new ClanNotification(notification.getTurnId(), notification.getClanId());
            completionNotification.setHeader(quest.getClan().getName());
            notificationService.addLocalizedTexts(completionNotification.getText(), "quest.completed", new String[] {},
                    quest.getDetails().getName());

            quest.setCompleted(true);

            // income
            clan.setFame(clan.getFame() + quest.getDetails().getFameReward());
            completionNotification.setFameIncome(quest.getDetails().getFameReward());
            clan.setCaps(clan.getCaps() + quest.getDetails().getCapsReward());
            completionNotification.setCapsIncome(quest.getDetails().getCapsReward());

            notificationService.save(completionNotification);
        }
    }

    private void handleQuestFailure(Quest quest, Character character, ClanNotification notification) {
        // update notification
        notificationService.addLocalizedTexts(notification.getText(), "character.quest.failure", new String[] {},
                quest.getDetails().getName());

        // award experience
        character.setExperience(character.getExperience() + 1);
        notification.setExperience(1);
    }

    private NotificationDetail getActionRollDetail(int difficulty, int roll, int result) {
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.action.roll",
                new String[] {String.valueOf(difficulty), String.valueOf(roll), String.valueOf(result)});
        return detail;
    }

}
