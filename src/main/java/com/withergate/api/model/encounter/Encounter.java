package com.withergate.api.model.encounter;

import com.withergate.api.model.location.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @Column(name = "encounter_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EncounterType type;

    @Column(name = "reward_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardType reward;

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
