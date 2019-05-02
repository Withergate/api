package com.withergate.api.model.action;

import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Building action. Used for sending a character to a building or to construct a building.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "building_actions")
@Getter
@Setter
public class BuildingAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", unique = true, nullable = false, updatable = false)
    private Character character;

    @Enumerated(EnumType.STRING)
    @Column(name = "building", nullable = false)
    private BuildingDetails.BuildingName building;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;


    /**
     * Action type.
     */
    public enum Type {
        VISIT, CONSTRUCT
    }

}
