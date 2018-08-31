package com.withergate.api.model;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Weapon;
import lombok.Getter;
import lombok.Setter;

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
    private int id;

    @Column(name = "clan_name", nullable = false)
    private String name;

    @Column(name = "caps", nullable = false)
    private int caps;

    @Column(name = "junk", nullable = false)
    private int junk;

    @OneToMany(mappedBy = "clan")
    private List<Character> characters;

    @OneToMany(mappedBy = "clan")
    private List<Weapon> weapons;

}
