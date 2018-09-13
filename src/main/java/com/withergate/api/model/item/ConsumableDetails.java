package com.withergate.api.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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

    @JsonIgnore
    @Column(name = "effect", updatable = false, nullable = false)
    private int effect;

    @Column(name = "effect_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EffectType effectType;

    @JsonProperty("effect")
    public String getEffectText() {
        switch (effectType){
            case HEALING:
                return "Heals [" + effect + "] hitpoints to a wounded character.";
            default:
                return "Unknown effect.";
        }
    }

}
