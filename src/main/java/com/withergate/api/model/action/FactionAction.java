package com.withergate.api.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

/**
 * Faction action. Used for sending a character to perform any faction-related business.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class FactionAction extends BaseAction {

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "faction", nullable = false)
    private String faction;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.FACTION.name();
    }

    /**
     * Action type.
     */
    public enum Type {
        JOIN
    }

}
