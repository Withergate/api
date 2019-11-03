package com.withergate.api.model.request;

import com.withergate.api.model.research.ResearchDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Research request.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@ToString
public class ResearchRequest {

    private int characterId;
    private ResearchDetails.ResearchName research;

}
