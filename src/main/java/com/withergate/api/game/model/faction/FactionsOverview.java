package com.withergate.api.game.model.faction;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Factions overview. Dynamically computed.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class FactionsOverview {

    private int clanPoints;
    private int clanFame;
    private int totalPoints;
    private ClanFactionOverview clan;
    private List<ClanFactionOverview> clans;
    private List<FactionPointsOverview> factions;

    /**
     * Constructor.
     */
    public FactionsOverview() {
        this.clans = new ArrayList<>();
        this.factions = new ArrayList<>();
    }

}
