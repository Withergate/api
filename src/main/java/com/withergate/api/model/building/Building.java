package com.withergate.api.model.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.withergate.api.model.Clan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Building entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "buildings")
@Getter
@Setter
public class Building {

    @Id
    @Column(name = "building_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "progress", nullable = false)
    private int progress;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private BuildingDetails details;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;

    /**
     * Returns the work needed for next level.
     *
     * @return the work needed for next level
     */
    @JsonProperty("nextLevel")
    public int getNextLevelWork() {
        return (level + 1) * details.getCost();
    }

}
