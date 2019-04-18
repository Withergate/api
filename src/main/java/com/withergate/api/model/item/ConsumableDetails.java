package com.withergate.api.model.item;

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

import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * ConsumableDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "consumable_details")
@Getter
@Setter
public class ConsumableDetails extends ItemDetails {

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "consumable_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "consumable_description")
    private Map<String, LocalizedText> description;

    @Column(name = "effect", updatable = false, nullable = false)
    private int effect;

    @Column(name = "prereq", updatable = false, nullable = false)
    private int prereq;

    @Column(name = "effect_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EffectType effectType;

}
