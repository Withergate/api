package com.withergate.api.service.faction;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.model.faction.FactionsOverview;
import com.withergate.api.game.model.request.FactionRequest;
import com.withergate.api.service.action.Actionable;

import java.util.List;

/**
 * Faction service.
 *
 * @author Martin Myslik
 */
public interface FactionService extends Actionable<FactionRequest> {

    /**
     * Gets all factions.
     *
     * @return list of factions
     */
    List<Faction> getFactions();

    /**
     * Computes factions overview dynamically.
     *
     * @param clanId clan ID
     * @return factions overview
     */
    FactionsOverview getOverview(int clanId);

    /**
     * Handles fame distribution at the end of the game.
     */
    void handleFameDistribution(int turnId);

    /**
     * Returns best faction.
     *
     * @return faction
     */
    Faction getBestFaction();

    /**
     * Returns best clan for given factions.
     *
     * @param faction faction
     * @return clan
     */
    Clan getBestClan(Faction faction);

}
