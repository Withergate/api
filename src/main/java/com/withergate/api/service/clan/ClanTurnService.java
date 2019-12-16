package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;

/**
 * Clan turn service. Responsible for performing all clan end-turn updates.
 *
 * @author Martin Myslik
 */
public interface ClanTurnService {

    /**
     * Performs all clan turn updates.
     *
     * @param clan clan
     * @param turnId current turn
     */
    void performClanTurnUpdates(Clan clan, int turnId);

}
