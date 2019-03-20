package com.withergate.api.model.location;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.ClanNotification;

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
    private ClanNotification notification;
}
