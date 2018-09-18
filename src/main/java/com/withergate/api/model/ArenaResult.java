package com.withergate.api.model;

import com.withergate.api.model.character.Character;
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
