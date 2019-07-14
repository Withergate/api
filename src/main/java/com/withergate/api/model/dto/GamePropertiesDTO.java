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
    private final int healing;
    private final int starvation;
    private final int starvationFame;
    private final int disasterFailureThreshold;
    private final int disasterPartialSuccessThreshold;

    public GamePropertiesDTO(GameProperties properties) {
        this.maxTurns = properties.getMaxTurns();
        this.healing = properties.getHealing();
        this.starvation = properties.getStarvation();
        this.starvationFame = properties.getStarvationFame();
        this.disasterFailureThreshold = properties.getDisasterFailureThreshold();
        this.disasterPartialSuccessThreshold = properties.getDisasterPartialSuccessThreshold();
    }

}
