package com.withergate.api.game.model.dto;

import java.util.Map;

import com.withergate.api.game.model.notification.LocalizedText;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Faction overview DTO.
 *
 * @author Martin Myslik
 */
@Getter
@RequiredArgsConstructor
public class FactionMiniDTO {

    private final String iconUrl;
    private final Map<String, LocalizedText> name;
    private final int points;

}
