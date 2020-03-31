package com.withergate.api.game.model.action;

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

    @Override
    public String getDescriptor() {
        return ActionDescriptor.BUILDING.name();
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

}
