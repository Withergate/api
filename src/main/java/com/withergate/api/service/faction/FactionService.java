package com.withergate.api.service.faction;

import java.util.List;

import com.withergate.api.model.faction.Faction;
import com.withergate.api.model.faction.FactionsOverview;
import com.withergate.api.model.request.FactionRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Faction service.
 *
 * @author Martin Myslik
 */
public interface FactionService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveFactionAction(FactionRequest request, int clanId) throws InvalidActionException;

    /**
     * Gets all factions.
     *
     * @return list of factions
     */
    List<Faction> getFactions();

    /**
     * Processes all pending faction actions.
     *
     * @param turnId turn ID
     */
    void processFactionActions(int turnId);

    /**
     * Computes factions overview dynamically.
     *
     * @param clanId clan ID
     * @return factions overview
     */
    FactionsOverview getOverview(int clanId);

}
