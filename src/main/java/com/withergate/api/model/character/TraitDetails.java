package com.withergate.api.model.character;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trait_details")
@Getter
@Setter
public class TraitDetails {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier", updatable = false, nullable = false)
    private TraitName identifier;

    @Column(name = "trait_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public enum TraitName {
        FIGHTER, BUILDER, ASCETIC, STRONG
    }
}
