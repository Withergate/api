package com.withergate.api.model.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
public class WeaponDetails {

    @Id
    @Column(name = "weapon_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "combat", nullable = false)
    private int combat;

    @Column(name = "weapon_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType type;


}
