package com.withergate.api.service.profile;

import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.profile.model.HistoricalResult;

/**
 * Service for working with historical game results.
 *
 * @author Martin Myslik
 */
public interface HistoricalResultsService {

    /**
     * Saves the results for given era.
     *
     * @param clans clans
     */
    void saveResults(List<Clan> clans);

    /**
     * Loads all results for the provided player.
     *
     * @param playerId player ID
     * @return loaded results
     */
    List<HistoricalResult> loadResults(int playerId);

}
