package com.withergate.api.model.action;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.trade.TradeType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Resource trade action. Used for buying and selling clan's resources.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "resource_trade_actions")
@Getter
@Setter
public class ResourceTradeAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", nullable = false, updatable = false)
    private Character character;

    @Column(name = "trade_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TradeType type;

    @Column(name = "food", nullable = false)
    private int food;

    @Column(name = "junk", nullable = false)
    private int junk;

}
