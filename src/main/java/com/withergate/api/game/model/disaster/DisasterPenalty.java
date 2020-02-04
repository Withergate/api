package com.withergate.api.game.model.disaster;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Disaster penalty.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "disaster_penalties")
@Getter
@Setter
public class DisasterPenalty {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Column(name = "penalty_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type penaltyType;

    @ManyToOne
    @JoinColumn(name = "disaster")
    @JsonIgnore
    private DisasterDetails disaster;

    /**
     * Penalty type.
     */
    public enum Type {
        RESOURCES_LOSS, CHARACTER_INJURY, BUILDING_DESTRUCTION, ITEM_LOSS, FAME_LOSS, INFORMATION_LOSS
    }

}
