package com.withergate.api.model.faction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.Clan;
import com.withergate.api.model.notification.LocalizedText;
import com.withergate.api.model.view.Views;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Map;
import java.util.Set;

/**
 * Faction entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "factions")
@Getter
@Setter
public class Faction {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "faction_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "faction_description")
    private Map<String, LocalizedText> description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

    @OneToMany(mappedBy = "faction")
    @JsonIgnore
    @JsonView(Views.Internal.class)
    private Set<Clan> clans;

}
