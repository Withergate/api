package com.withergate.api.game.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Research action. Used for sending a character to a perform research task.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class ResearchAction extends BaseAction {

    @Column(name = "research", nullable = false)
    private String research;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.RESEARCH.name();
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
