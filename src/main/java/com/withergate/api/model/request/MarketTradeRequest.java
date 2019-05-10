package com.withergate.api.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Market trade request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class MarketTradeRequest {

    private int characterId;
    private int offerId;

}
