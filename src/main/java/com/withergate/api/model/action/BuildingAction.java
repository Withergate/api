package com.withergate.api.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

/**
 * Building action. Used for sending a character to a building or to construct a building.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class BuildingAction extends BaseAction {

    @Column(name = "building", nullable = false)
    private String building;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.BUILDING + "." + type;
    }

    /**
     * Action type.
     */
    public enum Type {
        VISIT, CONSTRUCT
    }

}
