package com.withergate.api.game.model.faction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.item.ItemCost;
import com.withergate.api.game.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Map;

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
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "faction_aid")
    private Map<String, LocalizedText> description;

    @Column(name = "aid_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type aidType;

    @Column(name = "in_lead", updatable = false, nullable = false)
    private boolean leading;

    @Column(name = "fame", updatable = false, nullable = false)
    private int fame;

    @Column(name = "faction_points", updatable = false, nullable = false)
    private int factionPoints;

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost;

    @Column(name = "faction_points_cost", updatable = false, nullable = false)
    private int factionPointsCost;

    @Column(name = "aid", updatable = false, nullable = false)
    private int aid;

    @Column(name = "num_aid", updatable = false, nullable = false)
    private int numAid;

    @Column(name = "health_cost", updatable = false, nullable = false)
    private boolean healthCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_cost", updatable = false, nullable = false)
    private ItemCost itemCost;

    @Column(name = "information_cost", updatable = false, nullable = false)
    private int informationCost;

    @ManyToOne
    @JoinColumn(name = "faction")
    @JsonIgnore
    private Faction faction;

    /**
     * Aid type.
     */
    public enum Type {
        RESOURCE_SUPPORT, FACTION_SUPPORT, HEALING_REWARD, ITEM_REWARD, CAPS_REWARD
    }

}
