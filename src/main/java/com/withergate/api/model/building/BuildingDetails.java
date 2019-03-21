package com.withergate.api.model.building;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Building details.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "building_details")
@Getter
@Setter
public class BuildingDetails {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier", updatable = false, nullable = false)
    private BuildingName identifier;

    @Column(name = "building_name", updatable = false, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "visitable", nullable = false)
    private boolean visitable;

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost; // cost per level

    public enum BuildingName {
        SICK_BAY, GMO_FARM, TRAINING_GROUNDS, MONUMENT
    }

}
