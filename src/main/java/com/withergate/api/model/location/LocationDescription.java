package com.withergate.api.model.location;

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
 * Location details entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "location_descriptions")
@Getter
@Setter
public class LocationDescription {

    @Id
    @Column(name = "location", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(name = "scouting", nullable = false)
    private boolean scouting;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "location_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "location_description")
    private Map<String, LocalizedText> description;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "location_info")
    private Map<String, LocalizedText> info;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
