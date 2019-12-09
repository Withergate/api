package com.withergate.api.model.character;

import java.util.Map;

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

/**
 * TraitDetails entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "trait_details")
@Getter
@Setter
public class TraitDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

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

    @Column(name = "bonus_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private BonusType bonusType;

    @Column(name = "bonus_text", updatable = false)
    private String bonusText;

    @Column(name = "optional", updatable = false)
    private boolean optional;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

}
