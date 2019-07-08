package com.withergate.api.model.dto;

import com.withergate.api.GameProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * GameProperties DTO.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class GamePropertiesDTO {

    private final int maxTurns;
    private final int disasterFailureThreshold;
    private final int disasterPartialSuccessThreshold;

    public GamePropertiesDTO(GameProperties properties) {
        this.maxTurns = properties.getMaxTurns();
        this.disasterFailureThreshold = properties.getDisasterFailureThreshold();
        this.disasterPartialSuccessThreshold = properties.getDisasterPartialSuccessThreshold();
    }

}
