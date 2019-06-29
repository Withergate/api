package com.withergate.api.model.disaster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.notification.LocalizedText;

import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Disaster solution.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "disaster_solutions")
@Getter
@Setter
public class DisasterSolution {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Column(name = "solution_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type solutionType;

    @Column(name = "difficulty", updatable = false, nullable = false)
    private int difficulty;

    @Column(name = "bonus", updatable = false, nullable = false)
    private int bonus;

    @Column(name = "junk_cost", updatable = false, nullable = false)
    private int junkCost;

    @Column(name = "caps_cost", updatable = false, nullable = false)
    private int capsCost;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_solution_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_solution_description")
    private Map<String, LocalizedText> description;

    @ManyToOne
    @JoinColumn(name = "disaster")
    @JsonIgnore
    private DisasterDetails disaster;

    /**
     * Solution type.
     */
    public enum Type {
        AUTOMATIC, COMBAT, CRAFTSMANSHIP, INTELLECT
    }

}
