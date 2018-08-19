package com.withergate.api.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.withergate.api.model.Clan;
import com.withergate.api.model.item.Weapon;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "characters")
@Getter
@Setter
@ToString(exclude = {"clan"})
public class Character {

    @Id
    @Column(name = "character_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clan_id", nullable = false)
    @JsonIgnore
    private Clan clan;

    @Column(name = "character_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterState state;

    /**
     * ATTRIBUTES
     */

    @Column(name = "combat", nullable = false)
    private int combat;

    @Column(name="scavenge", nullable = false)
    private int scavenge;

    @Column(name="craftsmanship", nullable = false)
    private int craftsmanship;

    @Column(name="charm", nullable = false)
    private int charm;

    @Column(name="intellect", nullable = false)
    private int intellect;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;

    /**
     * Returns the current combat value for the character.
     *
     * @return the current combat value
     */
    @JsonProperty("totalCombat")
    public int getTotalCombat() {
        int weaponCombat = 0;
        if (weapon != null) {
            weaponCombat = weapon.getWeaponDetails().getCombat();
        }

        return combat + weaponCombat;
    }

}
