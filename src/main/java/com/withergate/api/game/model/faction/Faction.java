package com.withergate.api.game.model.faction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.notification.LocalizedText;
import com.withergate.api.game.model.view.Views;
import lombok.Getter;
import lombok.Setter;

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

    @JsonIgnore
    @Column(name = "faction_points", nullable = false)
    private int points;

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

    @OneToMany(mappedBy = "faction", cascade = CascadeType.ALL)
    private List<FactionAid> factionAids;

    @JsonProperty("numClans")
    private int getNumClans() {
        return clans.size();
    }

    @JsonProperty("points")
    public int getPoints() {
        return clans.stream().mapToInt(Clan::getFactionPoints).sum();
    }

}
