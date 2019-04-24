package com.withergate.api.model.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

/**
 * WeaponDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@DiscriminatorValue("WEAPON")
@Getter
@Setter
public class WeaponDetails extends ItemDetails {

    @Column(name = "bonus", nullable = false)
    private int combat;

    @Column(name = "weapon_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType type;


}
