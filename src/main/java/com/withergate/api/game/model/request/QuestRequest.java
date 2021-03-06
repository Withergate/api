package com.withergate.api.game.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Quest request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class QuestRequest extends ActionRequest {

    private int questId;

}
