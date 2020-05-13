package com.withergate.api.game.model.action;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Arena action.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "actions")
@Getter
@Setter
public class ArenaAction extends BaseAction {

    @Override
    public String getDescriptor() {
        return ActionDescriptor.ARENA.name();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    @Override
    public void cancel() {
        // not needed
    }

}
