package com.withergate.api.service.encounter;

import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.character.Character;

/**
 * IEncounterService interface.
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
     * @return true if encounter was successful
     */
    boolean handleEncounter(ClanNotification notification, Character character, Location location);
}
