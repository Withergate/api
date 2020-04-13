package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * PublishOffer request. Used for publishing new market offers
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class PublishOfferRequest {

    private int itemId;
    private int price;
    private boolean intelligent;

}
