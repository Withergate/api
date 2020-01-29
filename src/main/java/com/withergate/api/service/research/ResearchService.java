package com.withergate.api.service.research;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.request.ResearchRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Research service.
 *
 * @author Martin Myslik
 */
public interface ResearchService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveResearchAction(ResearchRequest request, int clanId) throws InvalidActionException;

    /**
     * Processes all pending research actions.
     *
     * @param turnId turn ID
     */
    void processResearchActions(int turnId);

    /**
     * Assigns initial research to given clan.
     *
     * @param clan provided clan
     */
    void assignResearch(Clan clan);

}
