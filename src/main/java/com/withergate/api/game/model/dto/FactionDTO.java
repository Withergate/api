package com.withergate.api.game.model.dto;

import java.util.List;
import java.util.Map;

import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.model.faction.FactionAid;
import com.withergate.api.game.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Faction DTO.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class FactionDTO {

    private final String identifier;
    private final String imageUrl;
    private final String iconUrl;
    private final Map<String, LocalizedText> name;
    private final Map<String, LocalizedText> description;
    private final int numClans;
    private final int points;
    private List<FactionAid> factionAids;

    /**
     * Constructor.
     *
     * @param faction faction
     */
    public FactionDTO(Faction faction) {
        this.identifier = faction.getIdentifier();
        this.imageUrl = faction.getImageUrl();
        this.iconUrl = faction.getIconUrl();
        this.name = faction.getName();
        this.description = faction.getDescription();
        this.numClans = faction.getClans().size();
        this.points = faction.getPoints();
        this.factionAids = faction.getFactionAids();
    }

}
