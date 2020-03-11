package com.withergate.api.game.model.character;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Tavern offer entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "tavern_offers")
@Getter
@Setter
public class TavernOffer {

    @Id
    @Column(name = "offer_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "clan_id", nullable = false)
    @JsonIgnore
    private Clan clan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "character_id")
    private Character character;

    @Column(name = "price", updatable = false, nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    /**
     * Tavern offer state.
     */
    public enum State {
        AVAILABLE, HIRED, PROCESSED
    }
}
