package com.withergate.api.service.building;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.request.ActionRequest;
import com.withergate.api.game.model.request.BuildingRequest;
import com.withergate.api.service.action.Actionable;

import java.util.List;

/**
 * Building service interface.
 *
 * @author Martin Myslik
 */
public interface BuildingService extends Actionable<BuildingRequest> {

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
