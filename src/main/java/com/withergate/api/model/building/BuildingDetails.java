package com.withergate.api.model.building;

import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Map;

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

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "building_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "building_description")
    private Map<String, LocalizedText> description;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "building_info")
    private Map<String, LocalizedText> info;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "visitable", nullable = false)
    private boolean visitable;

    @Column(name = "visit_junk_cost")
    private int visitJunkCost;

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost; // cost per level

    /**
     * Building name. Declared as enum for easier code references and for restricting allowed database values.
     */
    public enum BuildingName {
        SICK_BAY, GMO_FARM, TRAINING_GROUNDS, MONUMENT, FORGE, WORKSHOP, RAGS_SHOP, QUARTERS, STUDY
    }

}
