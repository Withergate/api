package com.withergate.api.game.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Crafting action.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class CraftingAction extends BaseAction {

    @Column(name = "crafting_item", nullable = false)
    private String craftingItem;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.CRAFTING.name();
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public void cancel() {
        // not supported
    }

}
