package com.withergate.api.model.action;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.withergate.api.model.trade.MarketOffer;
import lombok.Getter;
import lombok.Setter;

/**
 * Market trade action. Used for buying an item from another clan.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class MarketTradeAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private MarketOffer offer;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.TRADE.name();
    }

}
