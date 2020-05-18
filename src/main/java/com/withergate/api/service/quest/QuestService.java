package com.withergate.api.service.quest;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.request.QuestRequest;
import com.withergate.api.service.action.Actionable;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Quest service interface.
 *
 * @author Martin Myslik
 */
public interface QuestService extends Actionable {

    /**
     * Assign new quests to the provided clan based on its information level.
     *
     * @param clan             the clan
     * @param notification     the notification to be updated
     * @param turn             turn
     */
    void assignQuests(Clan clan, ClanNotification notification, int turn);

    /**
     * Assign new quests to the provided clan based on its faction.
     *
     * @param clan             the clan
     * @param notification     the notification to be updated
     */
    void assignFactionQuests(Clan clan, ClanNotification notification);

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     */
    void saveQuestAction(QuestRequest request, int clanId) throws InvalidActionException;

}
