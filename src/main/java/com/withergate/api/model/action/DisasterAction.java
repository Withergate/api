package com.withergate.api.model.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "actions")
@Getter
@Setter
public class DisasterAction extends BaseAction {

    @JsonIgnore
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "identifier", nullable = false, updatable = false)
    private DisasterSolution solution;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.DISASTER.name();
    }

}
