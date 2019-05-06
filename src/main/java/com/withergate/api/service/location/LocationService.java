package com.withergate.api.service.location;

import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;

/**
 * Location service interface.
 *
 * @author Martin Myslik
 */
public interface LocationService {

    /**
     * Gets location description for given location type.
     *
     * @param location the location type
     * @return location description
     */
    LocationDescription getLocationDescription(Location location);

    /**
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveLocationAction(LocationAction action);

    /**
     * Handles all pending location actions.
     *
     * @param turnId turn ID
     */
    void processLocationActions(int turnId);

}
