package com.withergate.api.service.building;

import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.request.BuildingRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Building service interface.
 *
 * @author Martin Myslik
 */
public interface BuildingService {

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException;

    /**
     * Processes all pending building actions.
     *
     * @param turnId turn ID
     */
    void processBuildingActions(int turnId);

    /**
     * Activates all passive building bonuses for given clan.
     *
     * @param turnId turn ID
     * @param clan   clan
     */
    void processPassiveBuildingBonuses(int turnId, Clan clan);

    /**
     * Retrieves building details from database.
     *
     * @param name the name of the building
     * @return the loaded building details
     */
    BuildingDetails getBuildingDetails(String name);

    /**
     * Retrieves all buildings details from the database.
     *
     * @return the loaded list of buildings details.
     */
    List<BuildingDetails> getAllBuildingDetails();

}
