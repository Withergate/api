package com.withergate.api.service.encounter;

import com.withergate.api.game.model.encounter.SolutionType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.character.Character;

/**
 * EncounterService interface.
 *
 * @author Martin Myslik
 */
public interface EncounterService {

    /**
     * Handles random encounter.
     *
     * @param notification notification
     * @param character character
     * @param location location
     * @param turn turn
     * @return true if encounter was successful
     */
    boolean handleEncounter(ClanNotification notification, Character character, Location location, int turn);

    /**
     * Handles solution. Returns true if given character succeeded with the specified solution type.
     *
     * @param character character
     * @param type solution type
     * @param difficulty action difficulty
     * @param notification notification
     * @return true if action was successful
     */
    boolean handleSolution(Character character, SolutionType type, int difficulty, ClanNotification notification);
}
