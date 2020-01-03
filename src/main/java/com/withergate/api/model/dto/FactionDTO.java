package com.withergate.api.model.dto;

import java.util.Map;

import com.withergate.api.model.faction.Faction;
import com.withergate.api.model.notification.LocalizedText;
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

    public FactionDTO(Faction faction) {
        this.identifier = faction.getIdentifier();
        this.imageUrl = faction.getImageUrl();
        this.iconUrl = faction.getIconUrl();
        this.name = faction.getName();
        this.description = faction.getDescription();
    }


}
