package com.withergate.api.game.model.encounter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.withergate.api.game.model.location.Location;
import lombok.Getter;
import lombok.Setter;

/**
 * Encounter entity class.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "encounters")
@Getter
@Setter
public class Encounter {

    @Id
    @Column(name = "encounter_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "location", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(name = "min_turn", updatable = false, nullable = false)
    private int turn;

    @Column(name = "encounter_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private SolutionType type;

    @Column(name = "reward_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardType reward;

    @Column(name = "item", updatable = false, nullable = false)
    private String item;

    @Column(name = "penalty_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private PenaltyType penalty;

    @Column(name = "difficulty", updatable = false, nullable = false)
    private int difficulty;

    @Column(name = "description_text", updatable = false, nullable = false)
    private String descriptionText;

    @Column(name = "success_text", updatable = false, nullable = false)
    private String successText;

    @Column(name = "failure_text", updatable = false, nullable = false)
    private String failureText;

}
