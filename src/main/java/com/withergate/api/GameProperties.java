package com.withergate.api;

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

    private int initialClanSize;
    private int characterCost;

    private int neighborhoodLootProbability;
    private int neighborhoodEncounterProbability;
    private int neighborhoodJunkMultiplier;

    private int wastelandLootProbability;
    private int wastelandEncounterProbability;
    private int wastelandJunkMultiplier;

    private int cityLootProbability;
    private int cityEncounterProbability;
    private int cityJunkMultiplier;

    private int rareItemChance;

    private int arenaCaps;
    private int arenaFame;

}
