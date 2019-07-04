package com.withergate.api.model.item;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Consumable entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "consumables")
@Getter
@Setter
public class Consumable extends Item {

    @Id
    @Column(name = "consumable_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_identifier")
    private ConsumableDetails details;

    @Override
    public Map<String, LocalizedText> getName() {
        return details.getName();
    }
}
