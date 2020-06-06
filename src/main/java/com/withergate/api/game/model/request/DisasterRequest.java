package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Disaster request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class DisasterRequest extends ActionRequest {

    private String solution;

}
