package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Trait assignment request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class TraitRequest {

    private int characterId;
    private String traitName;
    private boolean immediate;

}
