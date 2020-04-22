package com.withergate.api.game.model.dto;

import java.util.List;

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
    private final int tavernRefreshPrice;
    private final int trainingPrice;
    private final int traitForgetPrice;
    private final int factionEntryFame;
    private final List<Integer> factionTurns;

    // loans
    private final int loanCaps;
    private final int loanPayback;
    private final int loanFame;

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
        this.tavernRefreshPrice = properties.getTavernRefreshPrice();
        this.trainingPrice = properties.getTrainingPrice();
        this.traitForgetPrice = properties.getTraitForgetPrice();
        this.factionEntryFame = properties.getFactionEntryFame();
        this.factionTurns = properties.getFactionTurns();
        this.loanCaps = properties.getLoanCaps();
        this.loanPayback = properties.getLoanPayback();
        this.loanFame = properties.getLoanFame();
    }

}
