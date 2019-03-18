package com.withergate.api.service.building;

import com.withergate.api.model.building.BuildingDetails;

import java.util.List;

/**
 * Building service interface.
 *
 * @author Martin Myslik
 */
public interface IBuildingService {

    void processBuildingActions(int turnId);

    void processPassiveBuildingBonuses(int turnId);

    BuildingDetails getBuildingDetails(BuildingDetails.BuildingName name);

    List<BuildingDetails> getAllBuildingDetails();

}
