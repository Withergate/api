package com.withergate.api.service.research;

import com.withergate.api.model.Clan;

/**
 * Research service.
 *
 * @author Martin Myslik
 */
public interface ResearchService {

    /**
     * Assigns initial research to given clan.
     *
     * @param clan provided clan
     */
    void assignResearch(Clan clan);

}
