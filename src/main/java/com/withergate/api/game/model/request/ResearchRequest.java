package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Research request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class ResearchRequest {

    private int characterId;
    private String research;

}
