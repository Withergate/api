package com.withergate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.view.Views;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 * Clan entity. Represent the player and all his/her resources.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "clans")
@Getter
@Setter
public class Clan {

    /**
     * Clan ID is set manually and matches the authenticated user's ID.
     */
    @Id
    @Column(name = "clan_id")
    @JsonView(Views.Public.class)
    private int id;

    @Column(name = "clan_name", nullable = false)
    @JsonView(Views.Public.class)
    private String name;

    @Column(name = "fame", nullable = false)
    @JsonView(Views.Public.class)
    private int fame;

    @Column(name = "caps", nullable = false)
    @JsonView(Views.Internal.class)
    private int caps;

    @Column(name = "junk", nullable = false)
    @JsonView(Views.Internal.class)
    private int junk;

    @Column(name = "food", nullable = false)
    @JsonView(Views.Internal.class)
    private int food;

    @Column(name = "arena", nullable = false)
    @JsonView(Views.Internal.class)
    private boolean arena;

    @OneToMany(mappedBy = "clan", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private List<Character> characters;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private List<Weapon> weapons;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private List<Consumable> consumables;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "identifier")
    @MapKeyClass(BuildingDetails.BuildingName.class)
    @MapKeyEnumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private Map<BuildingDetails.BuildingName, Building> buildings;

    /**
     * List of unconstructed buildings. This list is assembled dynamically and is not persisted.
     */
    @Transient
    private List<Building> unconstructedBuildings;

    /**
     * Returns the buildings as List.
     *
     * @return the list of buildings
     */
    @JsonProperty("buildings")
    public Collection<Building> getBuildingsAsList() {
        return buildings.values();
    }

}
