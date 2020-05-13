package com.withergate.api.game.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Clan combat action.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "actions")
@Getter
@Setter
public class ClanCombatAction extends BaseAction {

    @Column(name = "clan_id", nullable = false)
    private int targetId;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.CLAN_COMBAT.name();
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
