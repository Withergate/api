package com.withergate.api.game.model.research;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Research entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "research")
@Getter
@Setter
public class Research {

    @Id
    @Column(name = "research_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "progress", nullable = false)
    private int progress;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private ResearchDetails details;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;

}
