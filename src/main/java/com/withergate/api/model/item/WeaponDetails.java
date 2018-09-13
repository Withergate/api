package com.withergate.api.model.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * WeaponDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "weapon_details")
@Getter
@Setter
public class WeaponDetails extends ItemDetails {

    @Column(name = "combat", nullable = false)
    private int combat;

    @Column(name = "weapon_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType type;


}
