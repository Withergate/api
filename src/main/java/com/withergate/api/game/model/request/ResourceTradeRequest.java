package com.withergate.api.game.model.request;

import com.withergate.api.game.model.trade.TradeType;
import lombok.Getter;
import lombok.Setter;

/**
 * Resource trade request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class ResourceTradeRequest extends ActionRequest {

    private int food;
    private int junk;
    private TradeType type;

}
