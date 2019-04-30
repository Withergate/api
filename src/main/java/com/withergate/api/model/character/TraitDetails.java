package com.withergate.api.model.character;

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

import com.withergate.api.model.BonusType;
import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name = "trait_details")
@Getter
@Setter
public class TraitDetails {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier", updatable = false, nullable = false)
    private TraitName identifier;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "trait_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "trait_description")
    private Map<String, LocalizedText> description;

    @Column(name = "bonus", updatable = false, nullable = false)
    private int bonus;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public enum TraitName {
        FIGHTER, BUILDER, ASCETIC, HUNTER, HOARDER
    }
}
