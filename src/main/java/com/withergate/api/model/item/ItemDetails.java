package com.withergate.api.model.item;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "item_details")
@DiscriminatorColumn(name = "item_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class ItemDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "craftable", updatable = false, nullable = false)
    private boolean craftable;

    @Column(name = "rarity", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    @Column(name = "item_type", updatable = false, nullable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "item_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "item_description")
    private Map<String, LocalizedText> description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public enum Rarity {
        COMMON, RARE
    }

}
