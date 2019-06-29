package com.withergate.api.model.request;

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
public class DisasterRequest {

    private int characterId;
    private String solution;

}
