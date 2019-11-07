package com.withergate.api.model.research;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "identifier", updatable = false, nullable = false)
    private ResearchName identifier;

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

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "information_level", nullable = false)
    private int informationLevel;

    @Column(name = "cost", updatable = false, nullable = false)
    private int cost;

    /**
     * Research name. Declared as enum for easier code references and for restricting allowed database values.
     */
    public enum ResearchName {
        FORGERY, BEGGING, ARCHITECTURE, CULINARY, DECORATION
    }

}
