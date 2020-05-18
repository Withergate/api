package com.withergate.api.service.combat;

import com.withergate.api.game.model.request.ClanCombatRequest;
import com.withergate.api.service.action.Actionable;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Clan combat service. Handles attacks between clans.
 *
 * @author Martin Myslik
 */
public interface ClanCombatService extends Actionable {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId  clan ID
     */
    void saveClanCombatAction(ClanCombatRequest request, int clanId) throws InvalidActionException;

}
