package com.withergate.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails;
import com.withergate.api.model.research.ResearchDetails.ResearchName;
import com.withergate.api.model.view.Views;
import com.withergate.api.service.clan.ClanServiceImpl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @Column(name = "last_activity", nullable = false)
    @JsonView(Views.Public.class)
    private LocalDateTime lastActivity;

    @Column(name = "fame", nullable = false)
    @JsonView(Views.Public.class)
    private int fame;

    @Column(name = "caps", nullable = false)
    @JsonView(Views.Internal.class)
    private int caps;

    @Column(name = "junk", nullable = false)
    @JsonView(Views.Internal.class)
    private int junk;

    @Column(name = "information", nullable = false)
    @JsonView(Views.Internal.class)
    private int information;

    @Column(name = "information_level", nullable = false)
    @JsonView(Views.Internal.class)
    private int informationLevel;

    @Column(name = "food", nullable = false)
    @JsonView(Views.Internal.class)
    private int food;

    @Column(name = "arena", nullable = false)
    @JsonView(Views.Internal.class)
    private boolean arena;

    @Column(name = "disaster_progress", nullable = false)
    @JsonView(Views.Internal.class)
    private int disasterProgress;

    @OneToMany(mappedBy = "clan", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Character> characters;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Weapon> weapons;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Gear> gear;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Outfit> outfits;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Consumable> consumables;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "identifier")
    @MapKeyClass(BuildingDetails.BuildingName.class)
    @MapKeyEnumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private Map<BuildingDetails.BuildingName, Building> buildings;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "identifier")
    @MapKeyClass(ResearchDetails.ResearchName.class)
    @MapKeyEnumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private Map<ResearchDetails.ResearchName, Research> research;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private Set<Quest> quests;

    @Column(name = "default_action", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private DefaultAction defaultAction;

    /**
     * Constructor.
     */
    public Clan() {
        characters = new HashSet<>();
        buildings = new EnumMap<>(BuildingName.class);
        research = new EnumMap<>(ResearchName.class);
        weapons = new HashSet<>();
        outfits = new HashSet<>();
        gear = new HashSet<>();
        consumables = new HashSet<>();
    }

    /**
     * Gets the maximum number of characters that could be part of the clan.
     *
     * @return the population limit
     */
    @JsonProperty("populationLimit")
    @JsonView(Views.Internal.class)
    public int getPopulationLimit() {
        int limit = ClanServiceImpl.BASIC_POPULATION_LIMIT;

        if (buildings.containsKey(BuildingDetails.BuildingName.QUARTERS)) {
            limit += buildings.get(BuildingDetails.BuildingName.QUARTERS).getLevel();
        }

        return limit;
    }

    /**
     * Returns the buildings as List.
     *
     * @return the list of buildings
     */
    @JsonProperty("buildings")
    @JsonView(Views.Internal.class)
    public Collection<Building> getBuildingsAsList() {
        return buildings.values();
    }

    /**
     * Returns the research as List.
     *
     * @return the list of research records
     */
    @JsonProperty("research")
    @JsonView(Views.Internal.class)
    public Collection<Research> getResearchAsList() {
        return research.values();
    }

    /**
     * Returns the information needed for next level.
     *
     * @return the information needed for next level
     */
    @JsonProperty("nextInformationLevel")
    @JsonView(Views.Internal.class)
    public int getNextLevelInformation() {
        return (informationLevel + 1) * ClanServiceImpl.INFORMATION_QUOTIENT;
    }

    /**
     * Gets the total number of characters in clan. Used for public information.
     *
     * @return the total number of characters
     */
    @JsonProperty("numCharacters")
    @JsonView(Views.Public.class)
    public int getNumberOfCharacters() {
        return characters.size();
    }

    /**
     * Clan's default action.
     */
    public enum DefaultAction {
        REST, EXPLORE_NEIGHBORHOOD
    }

    /*
     * Setters.
     */
    public void changeFood(int food) {
        this.food += food;
    }

    public void changeJunk(int junk) {
        this.junk += junk;
    }

    public void changeCaps(int caps) {
        this.caps += caps;
    }

    public void changeFame(int fame) {
        this.fame += fame;
    }

    public void changeInformation(int information) {
        this.information += information;
    }

    public void changeDisasterProgress(int progress) {
        this.disasterProgress += progress;
    }

}
