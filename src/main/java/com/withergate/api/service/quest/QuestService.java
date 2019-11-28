package com.withergate.api.service.quest;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.service.exception.InvalidActionException;

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
     */
    void assignQuests(Clan clan, ClanNotification notification);

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     */
    void saveQuestAction(QuestRequest request, int clanId) throws InvalidActionException;

    /**
     * Processes all pending quest actions.
     *
     * @param turnId turn ID
     */
    void processQuestActions(int turnId);

}
