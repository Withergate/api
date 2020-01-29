package com.withergate.api.game.model.location;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.notification.ClanNotification;

import lombok.Getter;
import lombok.Setter;

/**
 * Arena result. DTO object.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class ArenaResult {

    private Character character;
    private boolean winner;
    private ClanNotification notification;
}
