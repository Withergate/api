package com.withergate.api.service.quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.QuestAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.service.encounter.ConditionValidator;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.quest.Quest;
import com.withergate.api.game.model.quest.QuestDetails;
import com.withergate.api.game.model.request.QuestRequest;
import com.withergate.api.game.repository.action.QuestActionRepository;
import com.withergate.api.game.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RandomService randomService;
    private final CharacterService characterService;
    private final EncounterService encounterService;
    private final ItemService itemService;

    @Override
    public void assignQuests(Clan clan, ClanNotification notification) {
        log.debug("Assigning new quests to the clan.");

        Set<String> identifiers = new HashSet<>();
        clan.getQuests().forEach(quest -> identifiers.add(quest.getDetails().getIdentifier()));

        // load details and filter existing/completed quests
        List<QuestDetails> questDetails = questDetailsRepository.findAll()
                .stream()
                .filter(details -> !identifiers.contains(details.getIdentifier()) && !details.isFactionSpecific())
                .collect(Collectors.toList());

        if (questDetails.isEmpty()) {
            log.error("No quests found for clan {}.", clan.getId());
            return;
        }

        // get random quest
        QuestDetails details = questDetails.get(randomService.getRandomInt(0, questDetails.size() - 1));
        assignQuest(details, clan, notification);
    }

    @Override
    public void assignFactionQuests(Clan clan, ClanNotification notification) {
        List<QuestDetails> detailsList = questDetailsRepository.findAllByFaction(clan.getFaction().getIdentifier());
        if (detailsList.isEmpty()) {
            log.error("No quests found for faction {}.", clan.getFaction().getIdentifier());
            return;
        }

        QuestDetails details = detailsList.iterator().next();
        assignQuest(details, clan, notification);
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
        ConditionValidator.checkActionCondition(character, quest.getDetails().getCondition(), quest.getDetails().getItemCost());

        // check price
        QuestDetails details = quest.getDetails();
        if (clan.getFood() < details.getFoodCost() || clan.getJunk() < details.getJunkCost()) {
            throw new InvalidActionException("Not enough resources to perform this action.");
        }
        clan.changeFood(-details.getFoodCost());
        clan.changeJunk(-details.getJunkCost());

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

    private void assignQuest(QuestDetails details, Clan clan, ClanNotification notification) {
        Quest quest = new Quest();
        quest.setDetails(details);
        quest.setCompleted(false);
        quest.setProgress(0);
        quest.setClan(clan);
        clan.getQuests().add(quest);

        NotificationDetail notificationDetail = new NotificationDetail();
        notificationService.addLocalizedTexts(notificationDetail.getText(), "detail.quest.assigned", new String[]{},
                details.getName());
        notification.getDetails().add(notificationDetail);
    }

    private void processQuestAction(QuestAction action, ClanNotification notification) {
        Character character = action.getCharacter();
        Quest quest = action.getQuest();

        if (action.getQuest().getDetails().isHealthCost()) {
            int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);
            character.changeHitpoints(-injury);
            notification.changeInjury(injury);
            if (character.getHitpoints() < 1) notification.setDeath(true);
        }

        if (quest.getDetails().getItemCost() != null) {
            itemService.deleteItem(character, quest.getDetails().getItemCost(), notification);
        }

        boolean success = encounterService.handleSolution(character, quest.getDetails().getType(), quest.getDetails().getDifficulty(),
                notification);

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

        // update progress notification
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.action.progress",
                new String[]{quest.getProgress() + " / " + quest.getDetails().getCompletion()});
        notification.getDetails().add(detail);

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
            clan.changeFactionPoints(quest.getDetails().getFactionReward());
            completionNotification.changeFactionPoints(quest.getDetails().getFactionReward());

            // check follow ups
            if (quest.getDetails().getFollowUp() != null) {
                QuestDetails followUpDetails = questDetailsRepository.getOne(quest.getDetails().getFollowUp());
                assignQuest(followUpDetails, clan, completionNotification);
            }

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

}
