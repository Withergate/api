package com.withergate.api.model.trade;

import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.Outfit;
import com.withergate.api.model.item.Weapon;

import lombok.Getter;
import lombok.Setter;

/**
 * Market offer entity.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class MarketOffer {

    private Weapon weapon;
    private Gear gear;
    private Consumable consumable;
    private Outfit outfit;
    private int price;

    public MarketOffer() {
        // Empty constructor for JPA
    }

    public MarketOffer(Weapon weapon) {
        this.weapon = weapon;
        price = weapon.getDetails().getPrice();
    }

    public MarketOffer(Outfit outfit) {
        this.outfit = outfit;
        price = outfit.getDetails().getPrice();
    }

    public MarketOffer(Gear gear) {
        this.gear = gear;
        price = gear.getDetails().getPrice();
    }

    public MarketOffer(Consumable consumable) {
        this.consumable = consumable;
        price = consumable.getDetails().getPrice();
    }

}
