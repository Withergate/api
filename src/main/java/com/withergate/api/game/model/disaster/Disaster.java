package com.withergate.api.game.model.disaster;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Disaster entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "disasters")
@Getter
@Setter
public class Disaster {

    @Id
    @Column(name = "disaster_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private DisasterDetails details;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Column(name = "turn", updatable = false, nullable = false)
    private int turn;

}
