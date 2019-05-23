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

    private int characterVeteranCost;
    private int characterRookieCost;

    private int rareItemChance;

    private int arenaCaps;
    private int arenaFame;

}
