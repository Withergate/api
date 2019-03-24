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

@Entity
@Table(name = "traits")
@Getter
@Setter
public class Trait {

    @Id
    @Column(name = "trait_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private TraitDetails details;

}
