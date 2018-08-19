package com.withergate.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clan request. Used when creating a new clan.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClanRequest {

    private String name;
}
