package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Crafting request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class CraftingRequest extends ActionRequest {

    private String item;

}
