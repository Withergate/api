package com.withergate.api.model.request;

import com.withergate.api.model.item.ItemType;
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
    private ItemType type;
    private int price;

}
