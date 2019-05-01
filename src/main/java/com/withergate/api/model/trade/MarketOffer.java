package com.withergate.api.model.trade;

import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.Weapon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketOffer {

    private Weapon weapon;
    private Gear gear;
    private Consumable consumable;
    private int price;

    public MarketOffer() {}

    public MarketOffer(Weapon weapon) {
        this.weapon = weapon;
        price = weapon.getDetails().getPrice();
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
