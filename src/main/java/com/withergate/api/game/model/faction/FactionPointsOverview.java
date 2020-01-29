package com.withergate.api.game.model.faction;

import java.util.Map;

import com.withergate.api.game.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Faction points overview. Dynamically computed.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class FactionPointsOverview {

    private String identifier;
    private String iconUrl;
    private Map<String, LocalizedText> name;
    private int points;
    private int fame;

}
