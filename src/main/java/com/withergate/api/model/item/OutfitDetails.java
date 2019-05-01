package com.withergate.api.model.item;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * OutfitDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@DiscriminatorValue("OUTFIT")
@Getter
@Setter
public class OutfitDetails extends ItemDetails {

    @Column(name = "bonus", updatable = false, nullable = false)
    private int armor;

}
