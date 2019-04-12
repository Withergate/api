package com.withergate.api.service.quest;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.quest.QuestDetails;
import com.withergate.api.repository.action.QuestActionRepository;
import com.withergate.api.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.CombatService;
import com.withergate.api.service.notification.NotificationService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Quest service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class QuestServiceImpl implements QuestService {

    private final QuestDetailsRepository questDetailsRepository;
    private final NotificationService notificationService;
    private final QuestActionRepository questActionRepository;
    private final CombatService combatService;
    private final ClanService clanService;

    public QuestServiceImpl(
            QuestDetailsRepository questDetailsRepository,
            NotificationService notificationService,
            QuestActionRepository questActionRepository,
            CombatService combatService,
            @Lazy  ClanService clanService
    ) {
        this.questDetailsRepository = questDetailsRepository;
        this.notificationService = notificationService;
        this.questActionRepository = questActionRepository;
        this.combatService = combatService;
        this.clanService = clanService;
    }

    @Override
    public void assignQuests(Clan clan, ClanNotification notification, int informationLevel) {
        List<QuestDetails> questDetails = questDetailsRepository.findAllByInformationLevel(informationLevel);
        log.debug("Assigning {} new quests to the clan.", questDetails.size());

        for (QuestDetails details : questDetails) {
            Quest quest = new Quest();
            quest.setClan(clan);
            quest.setCompleted(false);
            quest.setProgress(0);
            quest.setDetails(details);

            clan.getQuests().add(quest);

            // add notification
            NotificationDetail notificationDetail = new NotificationDetail();
            notificationService.addLocalizedTexts(notificationDetail.getText(), "detail.quest.assigned", new String[] {},
                    details.getName());
            notification.getDetails().add(notificationDetail);
        }
    }

    @Override
    public void saveQuestAction(QuestAction action) {
        questActionRepository.save(action);
    }

    @Transactional
    @Override
    public void processQuestActions(int turnId) {
        log.debug("Processing quest actions.");

        for (QuestAction action : questActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification();
            notification.setClanId(action.getCharacter().getClan().getId());
            notification.setTurnId(turnId);
            notification.setHeader(action.getCharacter().getName());

            // process single action
            processQuestAction(action, notification);

            notificationService.save(notification);

            // mark action as completed
            action.setState(ActionState.COMPLETED);
            questActionRepository.save(action);
        }
    }

    private void processQuestAction(QuestAction action, ClanNotification notification) {
        Character character = action.getCharacter();
        Quest quest = action.getQuest();

        switch (action.getQuest().getDetails().getType()) {
            case COMBAT:
                // create temporary encounter from quest
                Encounter encounter = new Encounter();
                encounter.setDifficulty(quest.getDetails().getDifficulty());
                encounter.setSuccessText("character.quest.success");
                encounter.setFailureText("character.quest.failure");

                boolean result = combatService.handleSingleCombat(notification, encounter, character);
                if (result) {
                    handleQuestSuccess(quest, 1, notification);
                }

                // experience is handled in the combat service
                break;
            case INTELLECT:
                notification.setExperience(1);
                character.setExperience(character.getExperience() + 1);

                notificationService.addLocalizedTexts(notification.getText(), "character.quest.success", new String[]{});
                handleQuestSuccess(quest, character.getIntellect(), notification);
                break;
            case CRAFTSMANSHIP:
                notification.setExperience(1);
                character.setExperience(character.getExperience() + 1);

                notificationService.addLocalizedTexts(notification.getText(), "character.quest.success", new String[]{});
                handleQuestSuccess(quest, character.getCraftsmanship(), notification);
                break;
            default:
                log.warn("Unknown quest type: {}", action.getQuest().getDetails().getType());

        }
    }

    private void handleQuestSuccess(Quest quest, int progressIncrease, ClanNotification notification) {
        quest.setProgress(quest.getProgress() + progressIncrease);

        Clan clan = quest.getClan();
        // quest has been completed
        if (quest.getProgress() >= quest.getDetails().getCompletion() && !quest.isCompleted()) {
            log.debug("Quest {} has been completed.", quest.getId());

            ClanNotification completionNotification = new ClanNotification();
            completionNotification.setClanId(notification.getClanId());
            completionNotification.setTurnId(notification.getTurnId());
            completionNotification.setHeader(quest.getClan().getName());
            notificationService.addLocalizedTexts(completionNotification.getText(), "quest.completed", new String[]{}, quest.getDetails().getName());

            quest.setCompleted(true);

            // income
            clan.setFame(clan.getFame() + quest.getDetails().getFameReward());
            completionNotification.setFameIncome(quest.getDetails().getFameReward());
            clan.setCaps(clan.getCaps() + quest.getDetails().getCapsReward());
            completionNotification.setCapsIncome(quest.getDetails().getCapsReward());

            notificationService.save(completionNotification);
        }

        clanService.saveClan(clan);
    }

}
