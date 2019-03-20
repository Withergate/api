package com.withergate.api.service.encounter;

import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.character.Character;

/**
 * IEncounterService interface.
 *
 * @author Martin Myslik
 */
public interface IEncounterService {

    void handleEncounter(ClanNotification notification, Character character, Location location);
}
