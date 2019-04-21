package com.withergate.api.model.item;

import com.withergate.api.model.notification.LocalizedText;

import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * GearDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "gear_details")
@Getter
@Setter
public class GearDetails extends ItemDetails {

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "gear_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "gear_description")
    private Map<String, LocalizedText> description;

    @Column(name = "bonus", updatable = false, nullable = false)
    private int bonus;

    @Column(name = "bonus_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private BonusType bonusType;

}
