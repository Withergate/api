package com.withergate.api.service.combat;

import java.util.List;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.location.ArenaResult;
import com.withergate.api.game.model.notification.ClanNotification;

/**
 * CombatService interface.
 *
 * @author Martin Myslik
 */
public interface CombatService {

    boolean handleSingleCombat(ClanNotification notification, int difficulty, Character character);

    boolean handleClanCombat(ClanNotification attackerNotification, ClanNotification defenderNotification,
                             Character attacker, Character defender);

    List<ArenaResult> handleArenaFights(List<Character> characters);
}
