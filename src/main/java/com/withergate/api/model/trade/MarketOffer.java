package com.withergate.api.model.trade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.withergate.api.model.Clan;
import com.withergate.api.model.item.ItemDetails;
import lombok.Getter;
import lombok.Setter;

/**
 * Market offer entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "market_offers")
@Getter
@Setter
public class MarketOffer {

    @Id
    @Column(name = "offer_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "price", nullable = false)
    private int price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private ItemDetails details;

    @Column(name = "item_id")
    private int itemId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Clan seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Clan buyer;

    /**
     * Market offer state.
     */
    public enum State {
        PUBLISHED, PENDING, SOLD
    }

}
