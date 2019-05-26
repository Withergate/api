package com.withergate.api.model.request;

import com.withergate.api.model.Clan;

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
}
