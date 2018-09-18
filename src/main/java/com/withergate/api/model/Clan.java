package com.withergate.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Consumable;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.view.Views;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Clan entity. Represent the player and all his/her resources.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "clans")
@Getter
@Setter
public class Clan {

    /**
     * Clan ID is set manually and matches the authenticated user's ID.
     */
    @Id
    @Column(name = "clan_id")
    @JsonView(Views.Public.class)
    private int id;

    @Column(name = "clan_name", nullable = false)
    @JsonView(Views.Public.class)
    private String name;

    @Column(name = "fame", nullable = false)
    @JsonView(Views.Public.class)
    private int fame;

    @Column(name = "caps", nullable = false)
    @JsonView(Views.Internal.class)
    private int caps;

    @Column(name = "junk", nullable = false)
    @JsonView(Views.Internal.class)
    private int junk;

    @Column(name = "arena", nullable = false)
    @JsonView(Views.Internal.class)
    private boolean arena;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private List<Character> characters;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private List<Weapon> weapons;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL)
    @JsonView(Views.Internal.class)
    private List<Consumable> consumables;

}
