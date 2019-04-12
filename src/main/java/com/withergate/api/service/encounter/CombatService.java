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
public interface CombatService {

    boolean handleSingleCombat(ClanNotification notification, Encounter encounter, Character character);

    List<ArenaResult> handleArenaFights(List<Character> characters);
}
