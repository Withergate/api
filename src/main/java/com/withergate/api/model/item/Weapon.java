package com.withergate.api.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Weapon entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "weapons")
@Getter
@Setter
public class Weapon {

    @Id
    @Column(name = "weapon_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "weapon_name")
    private WeaponDetails weaponDetails;

    @JsonIgnore
    @OneToOne(mappedBy = "weapon")
    private Character character;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;
}
