package com.withergate.api.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.research.Research;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clan statistics entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "clan_turn_statistics")
@NoArgsConstructor
@Getter
@Setter
public class ClanTurnStatistics {

    @Id
    @Column(name = "statistics_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "clan_id")
    private int clanId;

    @Column(name = "turn_id")
    private int turnId;

    @Column(name = "fame")
    private int fame;

    @Column(name = "food")
    private int food;

    @Column(name = "junk")
    private int junk;

    @Column(name = "buildings")
    private int buildings;

    @Column(name = "research")
    private int research;

    @Column(name = "quests")
    private int quests;

    /**
     * Constructor.
     */
    public ClanTurnStatistics(Clan clan, int turnId) {
        this.clanId = clan.getId();
        this.turnId = turnId;

        this.fame = clan.getFame();
        this.food = clan.getFood();
        this.junk = clan.getJunk();
        this.buildings = clan.getBuildings().stream().mapToInt(Building::getLevel).sum();
        this.research = (int) clan.getResearch().stream().filter(Research::isCompleted).count();
        this.quests = (int) clan.getQuests().stream().filter(Quest::isCompleted).count();
    }

}
