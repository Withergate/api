package com.withergate.api.model.action;

import com.withergate.api.model.research.ResearchDetails;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Research action. Used for sending a character to a perform research task.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class ResearchAction extends BaseAction {

    @Enumerated(EnumType.STRING)
    @Column(name = "research", nullable = false)
    private ResearchDetails.ResearchName research;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.RESEARCH.name();
    }

}
