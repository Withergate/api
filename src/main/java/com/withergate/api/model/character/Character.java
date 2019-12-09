package com.withergate.api.model.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BaseAction;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.service.clan.CharacterServiceImpl;
import lombok.Getter;
import lombok.Setter;

/**
 * Character entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "characters")
@Getter
@Setter
public class Character {

    @Id
    @Column(name = "character_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;

    @Column(name = "character_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterState state;

    // Level

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "experience", nullable = false)
    private int experience;

    // Attributes

    @Column(name = "hitpoints", nullable = false)
    private int hitpoints;

    @Column(name = "max_hitpoints", nullable = false)
    private int maxHitpoints;

    @Column(name = "combat", nullable = false)
    private int combat;

    @Column(name = "scavenge", nullable = false)
    private int scavenge;

    @Column(name = "craftsmanship", nullable = false)
    private int craftsmanship;

    @Column(name = "intellect", nullable = false)
    private int intellect;

    // Items

    @JsonIgnore
    @OneToMany(mappedBy = "character", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Item> items;

    // Traits

    @Column(name = "skill_points", nullable = false)
    private int skillPoints;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "character_id")
    private Set<Trait> traits;

    // Actions

    @JsonIgnore
    @OneToMany(mappedBy = "character", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BaseAction> actions;

    /**
     * Constructor.
     */
    public Character() {
        traits = new HashSet<>();
        items = new HashSet<>();
        state = CharacterState.READY;
        level = 1;
    }

    // Helper functions

    /**
     * Returns current action of the character.
     *
     * @return the current action
     */
    @JsonProperty("action")
    public Optional<BaseAction> getCurrentAction() {
        if (actions == null) return Optional.empty();
        return actions.stream().filter(a -> a.getState().equals(ActionState.PENDING)).findFirst();
    }

    /**
     * Returns the experience needed for next level.
     *
     * @return the experience needed for next level
     */
    @JsonProperty("nextLevel")
    public int getNextLevelExperience() {
        return level * CharacterServiceImpl.LEVEL_QUOTIENT;
    }

    /**
     * Returns the traits as List.
     *
     * @return the list of traits
     */
    @JsonProperty("traits")
    public Collection<Trait> getFilteredTraits() {
        if (clan == null) {
            return new ArrayList<>();
        }
        return traits.stream().filter(trait -> trait.getOrder() <= getMaxOrder()).collect(Collectors.toList());
    }

    public List<Trait> getActiveTraits() {
        return traits.stream().filter(Trait::isActive).collect(Collectors.toList());
    }

    /**
     * Gets the maximum available trait order.
     *
     * @return the max order
     */
    @JsonIgnore
    public long getMaxOrder() {
        if (clan == null) {
            return -1;
        }

        return 1 + traits.stream().filter(Trait::isActive).count()
                + clan.getBuildings().get(BuildingName.TRAINING_GROUNDS).getLevel();
    }

    /**
     * Returns the current combat value for the character.
     *
     * @return the current combat value
     */
    @JsonProperty("totalCombat")
    public int getTotalCombat() {
        int weaponCombat = 0;
        if (getWeapon() != null) {
            weaponCombat = getWeapon().getDetails().getBonus();
        }

        return getCombat() + weaponCombat;
    }

    // Item getters

    @JsonProperty("weapon")
    public Item getWeapon() {
        return items.stream().filter(item -> item.getDetails().getItemType().equals(ItemType.WEAPON)).findFirst().orElse(null);
    }

    @JsonProperty("gear")
    public Item getGear() {
        return items.stream().filter(item -> item.getDetails().getItemType().equals(ItemType.GEAR)).findFirst().orElse(null);
    }

    @JsonProperty("outfit")
    public Item getOutfit() {
        return items.stream().filter(item -> item.getDetails().getItemType().equals(ItemType.OUTFIT)).findFirst().orElse(null);
    }

    @JsonIgnore
    public Item getItem(ItemType type) {
        return items.stream().filter(item -> item.getDetails().getItemType().equals(type)).findFirst().orElse(null);
    }

    // Setters

    public void changeHitpoints(int hitpoints) {
        this.hitpoints += hitpoints;
    }

    public void changeExperience(int experience) {
        this.experience += experience;
    }

    // Enhanced getters

    public int getCombat() {
        return getStat(combat);
    }

    public int getScavenge() {
        return getStat(scavenge);
    }

    public int getCraftsmanship() {
        return getStat(craftsmanship);
    }

    public int getIntellect() {
        return getStat(intellect);
    }

    private int getStat(int value) {
        if (hitpoints < (maxHitpoints / 3.0)) { // below 33%
            value = value - 2;
        } else if (hitpoints < (maxHitpoints * 2 / 3.0)) { // below 33%
            value = value - 1;
        }

        if (value < 1) {
            value = 1;
        }
        return value;
    }

}
