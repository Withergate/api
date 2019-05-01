package com.withergate.api.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.Clan;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.view.Views;
import com.withergate.api.service.clan.CharacterServiceImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "clan_id", nullable = false)
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

    /**
     * LEVEL
     */
    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "experience", nullable = false)
    private int experience;

    /**
     * ATTRIBUTES
     */

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gear_id")
    private Gear gear;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "character_id")
    @MapKeyColumn(name = "identifier")
    @MapKeyClass(TraitDetails.TraitName.class)
    @MapKeyEnumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private Map<TraitDetails.TraitName, Trait> traits;

    public Character() {
        if (traits == null) traits = new HashMap<>();
    }

    /**
     * Returns the experience needed for next level
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
    public Collection<Trait> getTraitsAsList() {
        return traits.values();
    }

    /**
     * Returns the current combat value for the character.
     *
     * @return the current combat value
     */
    @JsonProperty("totalCombat")
    public int getTotalCombat() {
        int weaponCombat = 0;
        if (weapon != null) {
            weaponCombat = weapon.getDetails().getCombat();
        }

        return combat + weaponCombat;
    }

}
