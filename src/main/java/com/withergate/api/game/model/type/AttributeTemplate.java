package com.withergate.api.game.model.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Attribute template entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "attribute_templates")
@NoArgsConstructor
@Getter
@Setter
public class AttributeTemplate {

    @Id
    @Column(name = "template_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "attr_sum", updatable = false, nullable = false)
    private int sum;

    @Column(name = "combat", updatable = false, nullable = false)
    private int combat;

    @Column(name = "scavenge", updatable = false, nullable = false)
    private int scavenge;

    @Column(name = "craftsmanship", updatable = false, nullable = false)
    private int craftsmanship;

    @Column(name = "intellect", updatable = false, nullable = false)
    private int intellect;

    @Column(name = "template_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    /**
     * Attribute template type.
     */
    public enum Type {
        BALANCED, ECONOMY, SMART, COMBAT, RANDOM
    }

}
