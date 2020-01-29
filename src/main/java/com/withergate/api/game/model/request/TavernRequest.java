package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Tavern request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class TavernRequest {

    private int characterId;
    private int offerId;

}
