package com.withergate.api.model.action;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base action. Defines basic properties of every action entity.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@MappedSuperclass
abstract class BaseAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionState state;
}
