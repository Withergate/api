package com.withergate.api.profile.model.achievement;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.withergate.api.profile.model.localization.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Achievement details.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "achievement_details")
@Getter
@Setter
public class AchievementDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_type", updatable = false, nullable = false)
    private AchievementType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", updatable = false, nullable = false)
    private Rarity rarity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "achievement_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "achievement_description")
    private Map<String, LocalizedText> description;

    @Column(name = "value_number", nullable = false)
    private int valueNumber; // universal field representing some achievement data (numbers)

    @Column(name = "value_string", nullable = false)
    private String valueString; // universal field representing some achievement data (strings)

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

}
