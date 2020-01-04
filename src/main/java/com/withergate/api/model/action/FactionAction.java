package com.withergate.api.model.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.disaster.DisasterSolution;
import com.withergate.api.model.faction.FactionAid;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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

    @JsonIgnore
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "faction_aid", nullable = false, updatable = false)
    private FactionAid factionAid;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.FACTION.name();
    }

    /**
     * Action type.
     */
    public enum Type {
        JOIN, SUPPORT
    }

}
