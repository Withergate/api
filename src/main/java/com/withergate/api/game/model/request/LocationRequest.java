package com.withergate.api.game.model.request;

import com.withergate.api.game.model.action.LocationAction;
import com.withergate.api.game.model.location.Location;
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
public class LocationRequest extends ActionRequest {

    private Location location;
    private LocationAction.LocationActionType type;

}
