package com.withergate.api.game.model.action;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Resting action.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "actions")
@Getter
@Setter
public class RestingAction extends BaseAction {

    @Override
    public String getDescriptor() {
        return ActionDescriptor.RESTING.name();
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public void cancel() {
        // not needed
    }

}
