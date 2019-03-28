package com.withergate.api.model.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class ItemDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "rarity", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Rarity rarity;

}
