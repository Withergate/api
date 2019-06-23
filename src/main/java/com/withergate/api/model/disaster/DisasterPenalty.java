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
 * Disaster penalty.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "disaster_penalties")
@Getter
@Setter
public class DisasterPenalty {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_penalty_text")
    private Map<String, LocalizedText> text;

    @Column(name = "penalty_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type penalty;

    @ManyToOne
    @JoinColumn(name = "disaster")
    @JsonIgnore
    private DisasterDetails disaster;

    /**
     * Penalty type.
     */
    public enum Type {
        JUNK_LOSS, FOOD_LOSS
    }

}
