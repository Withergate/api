package com.withergate.api.model.dto;

import com.withergate.api.GameProperties;
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
    private final String turnTimes;
    private final int healing;
    private final int foodConsumption;
    private final int starvationInjury;
    private final int starvationFame;
    private final int disasterFailureThreshold;
    private final int disasterPartialSuccessThreshold;
    private final int buildingFame;

    /**
     * Constructor.
     */
    public GamePropertiesDTO(GameProperties properties) {
        this.maxTurns = properties.getMaxTurns();
        this.turnTimes = properties.getTurnTimes();
        this.healing = properties.getHealing();
        this.foodConsumption = properties.getFoodConsumption();
        this.starvationInjury = properties.getStarvationInjury();
        this.starvationFame = properties.getStarvationFame();
        this.disasterFailureThreshold = properties.getDisasterFailureThreshold();
        this.disasterPartialSuccessThreshold = properties.getDisasterPartialSuccessThreshold();
        this.buildingFame = properties.getBuildingFame();
    }

}
