package com.withergate.api.game.model.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clan fame statistics.
 *
 * @author Martin Myslik
 */
@Entity
@NoArgsConstructor
@Table(name = "fame_statistics")
@Getter
@Setter
public class FameStatistics {

    @Id
    @Column(name = "statistics_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fame")
    private int fame;

    @Column(name = "text_id", updatable = false, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "clan_clan_id")
    @JsonIgnore
    private Clan clan;

}
