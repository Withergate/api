package com.withergate.api.model.encounter;

import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "encounter_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private EncounterType type;

    @Column(name = "reward_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardType reward;

    @Column(name = "difficulty", updatable = false, nullable = false)
    private int difficulty;

    @Column(name = "description_text", updatable = false, nullable = false)
    private String descriptionText;

    @Column(name = "success_text", updatable = false, nullable = false)
    private String successText;

    @Column(name = "failure_text", updatable = false, nullable = false)
    private String failureText;

    public String getDescriptionText(Character character, Location location) {
        return enhanceText(descriptionText, character, location);
    }

    public String getSuccessText(Character character, Location location) {
        return enhanceText(successText, character, location);
    }

    public String getFailureText(Character character, Location location) {
        return enhanceText(failureText, character, location);
    }

    private String enhanceText(String text, Character character, Location location) {
        text = text.replaceAll("\\[CH\\]", "<b>" + character.getName() + "</b>");
        text = text.replaceAll("\\[L\\]", location.name());

        return text;
    }
}
