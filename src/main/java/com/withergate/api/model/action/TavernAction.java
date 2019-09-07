package com.withergate.api.model.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.character.TavernOffer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @JsonIgnore
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private TavernOffer offer;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.TAVERN.name();
    }

}
