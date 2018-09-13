package com.withergate.api.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.Clan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Consumable entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "consumables")
@Getter
@Setter
public class Consumable {

    @Id
    @Column(name = "consumable_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_name")
    private ConsumableDetails details;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;

}
