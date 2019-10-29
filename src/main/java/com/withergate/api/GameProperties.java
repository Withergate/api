package com.withergate.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Global properties. Used for tuning the game balance.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("game")
public class GameProperties {

    // main config
    private int maxTurns;
    private String turnTimes;

    // general
    private int healing;
    private int foodConsumption;
    private int starvationInjury;
    private int starvationFame;
    private int buildingFame;

    // disasters
    private List<Integer> disasterTurns;
    private int disasterVisibility;
    private int disasterFailureThreshold;
    private int disasterPartialSuccessThreshold;
    private int disasterResourceLoss;
    private int disasterFameLoss;
    private int disasterBuildingProgressLoss;

}
