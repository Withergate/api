package com.withergate.api.game.model.request;

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
public class BuildingRequest extends ActionRequest {

    private String building;

}
