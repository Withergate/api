package com.withergate.api.game.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.withergate.api.game.model.location.Location;
import lombok.Getter;
import lombok.Setter;

/**
 * Location action. Used for sending a character to a specified location.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class LocationAction extends BaseAction {

    @Column(name = "location", nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationActionType type;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.LOCATION + "." + type;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    @Override
    public void cancel() {
        // not needed
    }

    /**
     * Action type.
     */
    public enum LocationActionType {
        VISIT, SCOUT
    }

}
