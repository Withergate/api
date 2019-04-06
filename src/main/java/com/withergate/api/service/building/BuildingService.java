package com.withergate.api.service.building;

import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.building.BuildingDetails;

import java.util.List;

/**
 * Building service interface.
 *
 * @author Martin Myslik
 */
public interface BuildingService {

    /**
     * Saves the provided action
     *
     * @param action the action to be saved
     */
    void saveBuildingAction(BuildingAction action);

    /**
     * Processes all pending building actions.
     *
     * @param turnId turn ID
     */
    void processBuildingActions(int turnId);

    /**
     * Activates all passive building bonuses.
     *
     * @param turnId turn ID
     */
    void processPassiveBuildingBonuses(int turnId);

    /**
     * Retrieves building details from database.
     *
     * @param name the name of the building
     * @return the loaded building details
     */
    BuildingDetails getBuildingDetails(BuildingDetails.BuildingName name);

    /**
     * Retrieves all buildings details from the database.
     *
     * @return the loaded list of buildings details.
     */
    List<BuildingDetails> getAllBuildingDetails();

}
