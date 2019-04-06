package com.withergate.api.service.quest;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.notification.ClanNotification;

/**
 * Quest service interface.
 *
 * @author Martin Myslik
 */
public interface QuestService {

    /**
     * Assign new quests to the provided clan based on its information level.
     *
     * @param clan             the clan
     * @param notification     the notification to be updated
     * @param informationLevel the clan's information level
     */
    void assignQuests(Clan clan, ClanNotification notification, int informationLevel);

    /**
     * Saves the provided action
     *
     * @param action the action to be saved
     */
    void saveQuestAction(QuestAction action);

    /**
     * Processes all pending quest actions.
     *
     * @param turnId turn ID
     */
    void processQuestActions(int turnId);

}
