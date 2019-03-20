package com.withergate.api.service.encounter;

import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;

import java.util.List;

/**
 * ICombatService interface.
 *
 * @author Martin Myslik
 */
public interface ICombatService {

    boolean handleCombat(ClanNotification notification, Encounter encounter, Character character, Location location);

    List<ArenaResult> handleArenaFights(List<Character> characters);
}
