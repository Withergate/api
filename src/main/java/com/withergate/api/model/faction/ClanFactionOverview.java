package com.withergate.api.model.faction;

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

    private String name;
    private int points;
    private int fame;

}
