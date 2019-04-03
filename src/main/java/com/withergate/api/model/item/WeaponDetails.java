package com.withergate.api.model.item;

import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Map;

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

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "weapon_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "weapon_description")
    private Map<String, LocalizedText> description;

    @Column(name = "craftable", updatable = false, nullable = false)
    private boolean craftable;

    @Column(name = "combat", nullable = false)
    private int combat;

    @Column(name = "weapon_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType type;


}
