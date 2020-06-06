package com.withergate.api.service.research;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.request.ResearchRequest;
import com.withergate.api.service.action.Actionable;

/**
 * Research service.
 *
 * @author Martin Myslik
 */
public interface ResearchService extends Actionable<ResearchRequest> {

    /**
     * Assigns initial research to given clan.
     *
     * @param clan provided clan
     */
    void assignResearch(Clan clan);

}
