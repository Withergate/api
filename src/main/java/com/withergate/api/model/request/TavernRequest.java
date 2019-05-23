package com.withergate.api.model.request;

import com.withergate.api.model.action.TavernAction;

import lombok.Getter;
import lombok.Setter;

/**
 * Tavern request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class TavernRequest {

    private int characterId;
    private TavernAction.Type characterType;

}
