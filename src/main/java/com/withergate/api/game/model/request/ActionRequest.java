package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Action request.
 *
 * @author Martin Myslik
 */
@Setter
@Getter
public abstract class ActionRequest {

    private int characterId;

}
