package com.withergate.api.service.combat;

import com.withergate.api.game.model.request.ClanCombatRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Clan combat service. Handles attacks between clans.
 *
 * @author Martin Myslik
 */
public interface ClanCombatService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId  clan ID
     */
    void saveClanCombatAction(ClanCombatRequest request, int clanId) throws InvalidActionException;

    /**
     * Handles all pending combat actions.
     *
     * @param turnId turn ID
     */
    void processClanCombatActions(int turnId);

}
