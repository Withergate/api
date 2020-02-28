package com.withergate.api.profile.model.achievement;

/**
 * Achievement type.
 *
 * @author Martin Myslik
 */
public enum AchievementType {
    // general achievements
    CONSECUTIVE_LOGINS, PREMIUM,

    // action achievements

    // end-turn achievements
    ARENA_WINS, BUILDING_ALL, BUILDING_TOP, BUILDING_DEFENSE, RESEARCH_COUNT, CHARACTER_COUNT, INFORMATION_LEVEL,

    // end-game achievements
    NO_STARVATION, DISASTERS_AVERTED, GAME_COUNT, CRAFT_COUNT, ATTACK_SUCCESS_COUNT, DEFENSE_SUCCESS_COUNT

    // ranking achievements
}
