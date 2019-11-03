package com.withergate.api.service.research;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ResearchAction;

/**
 * Research service.
 *
 * @author Martin Myslik
 */
public interface ResearchService {

    /**
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveResearchAction(ResearchAction action);

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
