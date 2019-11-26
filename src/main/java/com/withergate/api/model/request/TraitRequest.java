package com.withergate.api.model.request;

import com.withergate.api.model.character.TraitDetails.TraitName;
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
    private TraitName traitName;

}
