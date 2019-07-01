package com.withergate.api.service.encounter;

import java.util.List;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.notification.ClanNotification;

/**
 * ICombatService interface.
 *
 * @author Martin Myslik
 */
public interface CombatService {

    boolean handleSingleCombat(ClanNotification notification, int difficulty, Character character);

    List<ArenaResult> handleArenaFights(List<Character> characters);
}
