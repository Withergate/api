package com.withergate.api.game.model.arena;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Arena statistics entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "arena_stats")
@Getter
@Setter
public class ArenaStats {

    @Id
    @Column(name = "stats_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "character_name", updatable = false, nullable = false)
    private String characterName;

    @Column(name = "clan_name", updatable = false, nullable = false)
    private String clanName;

    @Column(name = "stats", updatable = true, nullable = false)
    private int stats;

}
