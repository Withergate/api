package com.withergate.api.game.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clan statistics.
 *
 * @author Martin Myslik
 */
@Entity
@NoArgsConstructor
@Table(name = "clan_statistics")
@Getter
@Setter
public class ClanStatistics {

    @Id
    @Column(name = "statistics_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "starvations")
    private int starvations;

    @Column(name = "failed_disasters")
    private int failedDisasters;

    @Column(name = "crafted_items")
    private int craftedItems;

    @Column(name = "encounters")
    private int encounters;

    @Column(name = "encounters_success")
    private int encountersSuccess;

    @Column(name = "incoming_attacks")
    private int incomingAttacks;

    @Column(name = "incoming_attacks_success")
    private int incomingAttacksSuccess;

    @Column(name = "outcoming_attacks")
    private int outcomingAttacks;

    @Column(name = "outcoming_attacks_success")
    private int outcomingAttacksSuccess;

    @JsonIgnore
    @OneToOne
    @MapsId
    private Clan clan;

}
