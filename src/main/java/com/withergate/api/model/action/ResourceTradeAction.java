package com.withergate.api.model.action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.withergate.api.model.trade.TradeType;
import lombok.Getter;
import lombok.Setter;

/**
 * Resource trade action. Used for buying and selling clan's resources.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class ResourceTradeAction extends BaseAction {

    @Column(name = "trade_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TradeType type;

    @Column(name = "food", nullable = false)
    private int food;

    @Column(name = "junk", nullable = false)
    private int junk;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.TRADE.name();
    }

}
