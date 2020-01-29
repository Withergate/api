package com.withergate.api.game.model.combat;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.notification.NotificationDetail;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity for storing the result of a combat round.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class CombatResult {

    private Character winner;
    private Character loser;
    private List<NotificationDetail> details;
    private boolean finished;

    public CombatResult() {
        details = new ArrayList<>();
    }

}
