package com.withergate.api.model.turn;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Turn entity. Represent one turn of the game.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "turns")
@Getter
@Setter
public class Turn {

    /**
     * Turn ID is incremented manually.
     */
    @Id
    @Column(name = "turn_id")
    private int turnId;
}
