package com.withergate.api.profile.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * Historical result entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "historical_results")
@Getter
@Setter
public class HistoricalResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

    // CLAN INFO

    @Column(name = "clan_name")
    private String clanName;

    @Column(name = "place")
    private int place;

    @Column(name = "fame")
    private int fame;

    // ERA INFO

    @Column(name = "num_clans")
    private int numClans;

    @Column(name = "num_turns")
    private int numTurns;

    @Column(name = "game_ended")
    private LocalDate gameEnded;

    @Column(name = "game_version")
    private String gameVersion;

}
