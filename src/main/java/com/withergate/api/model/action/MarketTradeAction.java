package com.withergate.api.model.action;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.trade.MarketOffer;
import lombok.Getter;
import lombok.Setter;

/**
 * Market trade action. Used for buying an item from another clan.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "market_trade_actions")
@Getter
@Setter
public class MarketTradeAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", nullable = false, updatable = false)
    private Character character;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "offer_id", nullable = false, updatable = false)
    private MarketOffer offer;

}
