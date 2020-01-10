package com.withergate.api.model.faction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Faction aid entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "faction_aids")
@Getter
@Setter
public class FactionAid {

    @Id
    @Column(name = "aid_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "aid_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type aidType;

    @Column(name = "fame", updatable = false, nullable = false)
    private int fame;

    @Column(name = "faction_points", updatable = false, nullable = false)
    private int factionPoints;

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost;

    @Column(name = "aid", updatable = false, nullable = false)
    private int aid;

    @Column(name = "num_aid", updatable = false, nullable = false)
    private int numAid;

    @Column(name = "health_cost", updatable = false, nullable = false)
    private boolean healthCost;

    @ManyToOne
    @JoinColumn(name = "faction")
    @JsonIgnore
    private Faction faction;

    /**
     * Aid type.
     */
    public enum Type {
        RESOURCES, SACRIFICE
    }

}
