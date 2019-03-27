package com.withergate.api.model.request;

import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.location.Location;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Exploration request. Specifies an exploration action for the specified character.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class LocationRequest {

    private int characterId;
    private Location location;
    private LocationAction.LocationActionType type;

}
