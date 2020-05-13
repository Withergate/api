package com.withergate.api.game.model.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.disaster.DisasterSolution;

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
    @JoinColumn(name = "disaster_solution", nullable = false, updatable = false)
    private DisasterSolution solution;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.DISASTER.name();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    @Override
    public void cancel() {
        Clan clan = getCharacter().getClan();

        clan.changeCaps(solution.getCapsCost());
        clan.changeFood(solution.getFoodCost());
        clan.changeJunk(solution.getJunkCost());
    }

}
