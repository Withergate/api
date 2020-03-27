package com.withergate.api.game.model.request;

import com.withergate.api.game.model.type.AttributeTemplate.Type;
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
    private Type type;

}
