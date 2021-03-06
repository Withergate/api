package com.withergate.api.game.model.building;

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

import com.withergate.api.game.model.notification.LocalizedText;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.type.PassiveBonusType;
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
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

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

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost; // cost per level

    @Column(name = "bonus", updatable = false, nullable = false)
    private int bonus;

    @Column(name = "bonus_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private BonusType bonusType;

    @Column(name = "bonus_text", updatable = false)
    private String bonusText;

    @Column(name = "passive_bonus_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private PassiveBonusType passiveBonusType;

    @Column(name = "passive_bonus_text", updatable = false)
    private String passiveBonusText;

    @Column(name = "passive_bonus", updatable = false, nullable = false)
    private int passiveBonus;

}
