package com.withergate.api.model.action;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.disaster.DisasterSolution;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Disaster action. Used for sending a character to participate in a disaster solution.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "disaster_actions")
@Getter
@Setter
public class DisasterAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", nullable = false, updatable = false)
    private Character character;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "identifier", nullable = false, updatable = false)
    private DisasterSolution solution;

}
