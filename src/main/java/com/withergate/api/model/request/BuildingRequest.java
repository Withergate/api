package com.withergate.api.model.request;

import com.withergate.api.model.action.BuildingAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Building request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class BuildingRequest {

    private int characterId;
    private String building;
    private BuildingAction.Type type;

}
