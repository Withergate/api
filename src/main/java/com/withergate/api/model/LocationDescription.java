package com.withergate.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "location_descriptions")
@Getter
@Setter
public class LocationDescription {

    @Id
    @Column(name = "location", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
