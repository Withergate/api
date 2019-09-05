package com.withergate.api.model.action;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.withergate.api.model.character.TavernOffer;
import lombok.Getter;
import lombok.Setter;

/**
 * Tavern action.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class TavernAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private TavernOffer offer;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.TAVERN.name();
    }

}
