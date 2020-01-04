package com.withergate.api.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.dto.FactionDTO;
import com.withergate.api.model.faction.Faction;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.view.Views;
import com.withergate.api.service.clan.ClanServiceImpl;
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

    @Column(name = "disaster_progress", nullable = false)
    @JsonView(Views.Internal.class)
    private int disasterProgress;

    @OneToMany(mappedBy = "clan", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Character> characters;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Item> items;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Building> buildings;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Research> research;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Quest> quests;

    @Column(name = "default_action", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private DefaultAction defaultAction;

    @Column(name = "prefer_disaster", nullable = false)
    @JsonView(Views.Internal.class)
    private boolean preferDisaster;

    /**
     * Constructor.
     */
    public Clan() {
        characters = new HashSet<>();
        buildings = new HashSet<>();
        research = new HashSet<>();
        items = new HashSet<>();
        quests = new HashSet<>();
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

        Optional<Building> building = buildings.stream().filter(b -> b.getDetails().getEndBonusType() != null
                && b.getDetails().getEndBonusType().equals(EndBonusType.POPULATION)).findFirst();
        if (building.isPresent()) {
            limit += building.get().getLevel();
        }

        return limit;
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

    /**
     * Faction.
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "faction")
    private Faction faction;

    @Column(name = "faction_points", nullable = false)
    private int factionPoints;

    /**
     * Gets faction as DTO.
     *
     * @return faction DTO
     */
    @JsonProperty("faction")
    @JsonView(Views.Public.class)
    public FactionDTO getFactionDTO() {
        return faction == null ? null : new FactionDTO(faction);
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

    public void changeFactionPoints(int factionPoints) {
        this.factionPoints += factionPoints;
    }

    /*
     * Additional getters
     */
    public Building getBuilding(String identifier) {
        return buildings.stream().filter(building -> building.getDetails().getIdentifier().equals(identifier))
                .findFirst().orElse(null);
    }

    public Research getResearch(ResearchBonusType bonusType) {
        return research.stream().filter(r -> r.getDetails().getBonusType().equals(bonusType))
                .findFirst().orElse(null);
    }

    public Research getResearch(String identifier) {
        return research.stream().filter(r -> r.getDetails().getIdentifier().equals(identifier))
                .findFirst().orElse(null);
    }

}
