package com.withergate.api.game.model.research;

import com.withergate.api.game.model.ResearchBonusType;
import com.withergate.api.game.model.notification.LocalizedText;
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
 * Research details.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "research_details")
@Getter
@Setter
public class ResearchDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "bonus_type", updatable = false, nullable = false)
    private ResearchBonusType bonusType;

    @Column(name = "bonus_text", updatable = false)
    private String bonusText;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "research_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "research_description")
    private Map<String, LocalizedText> description;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "research_info")
    private Map<String, LocalizedText> info;

    @Column(name = "value", nullable = false)
    private int value; // universal field representing some research data

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "information_level", nullable = false)
    private int informationLevel;

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost;

    @Column(name = "fame", updatable = false, nullable = false)
    private int fame;

}
