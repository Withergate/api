package com.withergate.api.model.character;

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
 * Trait entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "traits")
@Getter
@Setter
public class Trait {

    @Id
    @Column(name = "trait_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "trait_order", updatable = false, nullable = false)
    private int order;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private TraitDetails details;

}
