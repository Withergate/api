package com.withergate.api.service.combat;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.combat.CombatResult;
import com.withergate.api.model.notification.ClanNotification;

/**
 * CombatRound service interface.
 *
 * @author Martin Myslik
 */
public interface CombatRoundService {

    CombatResult handleCombatRound(Character character1, ClanNotification notification1, Character character2,
                                   ClanNotification notification2, int round);

}
