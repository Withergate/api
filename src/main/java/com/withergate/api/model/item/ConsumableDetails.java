package com.withergate.api.model.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

/**
 * ConsumableDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@DiscriminatorValue("CONSUMABLE")
@Getter
@Setter
public class ConsumableDetails extends ItemDetails {

    @Column(name = "bonus", updatable = false, nullable = false)
    private int effect;

    @Column(name = "prereq", updatable = false, nullable = false)
    private int prereq;

    @Column(name = "effect_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EffectType effectType;

}
