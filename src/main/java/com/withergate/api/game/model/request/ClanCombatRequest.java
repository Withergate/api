package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Clan attack request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class ClanCombatRequest {

    private int characterId;
    private int targetId;

}