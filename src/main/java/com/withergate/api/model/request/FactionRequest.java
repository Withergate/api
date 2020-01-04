package com.withergate.api.model.request;

import com.withergate.api.model.action.FactionAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Faction request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class FactionRequest {

    private int characterId;
    private FactionAction.Type type;
    private String faction;
    private int factionAid;

}
