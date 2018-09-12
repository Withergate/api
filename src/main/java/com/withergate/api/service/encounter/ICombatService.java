package com.withergate.api.service.encounter;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;

/**
 * ICombatService interface.
 *
 * @author Martin Myslik
 */
public interface ICombatService {

    boolean handleCombat(ClanNotification notification, Encounter encounter, Character character, Location location);
}
