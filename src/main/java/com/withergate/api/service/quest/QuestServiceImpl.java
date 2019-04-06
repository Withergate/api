package com.withergate.api.service.quest;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.quest.QuestDetails;
import com.withergate.api.repository.action.QuestActionRepository;
import com.withergate.api.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.notification.NotificationService;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public QuestServiceImpl(QuestDetailsRepository questDetailsRepository,
                            NotificationService notificationService,
                            QuestActionRepository questActionRepository) {
        this.questDetailsRepository = questDetailsRepository;
        this.notificationService = notificationService;
        this.questActionRepository = questActionRepository;
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
            notificationService.addLocalizedTexts(notificationDetail.getText(), "detail.quest.assigned", new String[]{}, details.getName());
            notification.getDetails().add(notificationDetail);
        }
    }

    @Override
    public void saveQuestAction(QuestAction action) {
        questActionRepository.save(action);
    }

    @Override
    public void processQuestActions(int turnId) {

    }

}
