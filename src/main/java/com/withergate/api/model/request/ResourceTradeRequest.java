package com.withergate.api.model.request;

import com.withergate.api.model.trade.TradeType;

import lombok.Getter;
import lombok.Setter;

/**
 * Resource trade request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class ResourceTradeRequest {

    private int characterId;
    private int food;
    private int junk;
    private TradeType type;

}
