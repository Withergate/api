package com.withergate.api.model.item;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Gear entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "gear")
@Getter
@Setter
public class Gear extends Item {

    @Id
    @Column(name = "gear_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_identifier")
    private GearDetails details;

    @JsonIgnore
    @OneToOne(mappedBy = "gear")
    private Character character;

    @Override
    public Map<String, LocalizedText> getName() {
        return details.getName();
    }
}
