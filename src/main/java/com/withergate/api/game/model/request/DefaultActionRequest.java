package com.withergate.api.game.model.request;

import com.withergate.api.game.model.Clan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DefaultAction request. Used when changing clan's default action.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DefaultActionRequest {

    private Clan.DefaultAction defaultAction;
    private boolean preferDisaster;

}
