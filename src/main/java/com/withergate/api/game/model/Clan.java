package com.withergate.api.game.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.dto.FactionDTO;
import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.quest.Quest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.statistics.ClanStatistics;
import com.withergate.api.game.model.statistics.FameStatistics;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.model.view.Views;
import com.withergate.api.service.clan.ClanServiceImpl;
import com.withergate.api.service.combat.ClanDefenseUtils;
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

    @Column(name = "active_loan", nullable = false)
    @JsonView(Views.Internal.class)
    private boolean activeLoan;

    @OneToMany(mappedBy = "clan", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private Set<Character> characters;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<Item> items;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private Set<Building> buildings;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private Set<Research> research;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView(Views.Internal.class)
    private Set<Quest> quests;

    @JsonView(Views.Internal.class)
    @OneToOne(mappedBy = "clan", cascade = CascadeType.ALL)
    private ClanStatistics statistics;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private Set<FameStatistics> fameStatistics;

    /**
     * Constructor.
     */
    public Clan() {
        characters = new HashSet<>();
        buildings = new HashSet<>();
        research = new HashSet<>();
        items = new HashSet<>();
        quests = new HashSet<>();
        fameStatistics = new HashSet<>();

        // statistics
        ClanStatistics statistics = new ClanStatistics();
        statistics.setClan(this);
        setStatistics(statistics);
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

        Optional<Building> building = buildings.stream().filter(b -> b.getDetails().getPassiveBonusType() != null
                && b.getDetails().getPassiveBonusType().equals(PassiveBonusType.POPULATION)).findFirst();
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
     * Faction.
     */
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "faction")
    private Faction faction;

    @Column(name = "faction_points", nullable = false)
    private int factionPoints;

    @Column(name = "defender_name", nullable = false)
    private String defenderName;

    @JsonProperty("defender")
    @JsonView(Views.Internal.class)
    public Character getDefender() {
        return ClanDefenseUtils.getDefender(this);
    }

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

    public void changeDisasterProgress(int progress) {
        this.disasterProgress += progress;
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
