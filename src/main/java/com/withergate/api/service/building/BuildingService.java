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

    void processBuildingActions(int turnId);

    void processPassiveBuildingBonuses(int turnId);

    BuildingDetails getBuildingDetails(BuildingDetails.BuildingName name);

    List<BuildingDetails> getAllBuildingDetails();

}
