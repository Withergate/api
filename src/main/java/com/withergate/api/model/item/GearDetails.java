package com.withergate.api.model.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.withergate.api.model.BonusType;
import lombok.Getter;
import lombok.Setter;

/**
 * GearDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@DiscriminatorValue("GEAR")
@Getter
@Setter
public class GearDetails extends ItemDetails {

    @Column(name = "bonus", updatable = false, nullable = false)
    private int bonus;

    @Column(name = "bonus_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private BonusType bonusType;

}
