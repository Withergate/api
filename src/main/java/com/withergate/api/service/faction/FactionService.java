package com.withergate.api.service.faction;

import com.withergate.api.model.faction.Faction;

import java.util.List;

/**
 * Faction service.
 *
 * @author Martin Myslik
 */
public interface FactionService {

    /**
     * Gets all factions.
     *
     * @return list of factions
     */
    List<Faction> getFactions();

}
