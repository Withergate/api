package com.withergate.api.model.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.withergate.api.model.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

/**
 * Base action. Defines basic properties of every action entity.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@Entity(name = "actions")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public abstract class BaseAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionState state;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", nullable = false, updatable = false)
    private Character character;

    /**
     * Returns a unique descriptor for the implemented action type.
     *
     * @return the action descriptor
     */
    @JsonProperty("descriptor")
    public abstract String getDescriptor();

}
