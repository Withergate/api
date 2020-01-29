package com.withergate.api.game.model.item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import lombok.Getter;
import lombok.Setter;

/**
 * Item entity. Base class for all item types.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "items")
@Getter
@Setter
public class Item {

    @Id
    @Column(name = "item_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private ItemDetails details;

    @ManyToOne
    @JoinColumn(name = "character_id")
    @JsonIgnore
    private Character character;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;


}
