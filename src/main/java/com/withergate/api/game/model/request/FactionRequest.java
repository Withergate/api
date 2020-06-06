package com.withergate.api.game.model.request;

import com.withergate.api.game.model.action.FactionAction;
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
public class FactionRequest extends ActionRequest {

    private FactionAction.Type type;
    private String faction;
    private String factionAid;

}
