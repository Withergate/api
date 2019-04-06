package com.withergate.api.model.request;

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
public class QuestRequest {

    private int characterId;
    private int questId;

}
