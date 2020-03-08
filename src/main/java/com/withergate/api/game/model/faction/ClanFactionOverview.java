package com.withergate.api.game.model.faction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Clan faction overview.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@Getter
@Setter
public class ClanFactionOverview {

    private int clanId;
    private String name;
    private int points;
    private int fame;

}
