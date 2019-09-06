package com.withergate.api.model.action;

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

}
