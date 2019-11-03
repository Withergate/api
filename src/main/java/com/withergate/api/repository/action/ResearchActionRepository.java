package com.withergate.api.repository.action;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResearchAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ResearchAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ResearchActionRepository extends JpaRepository<ResearchAction, Integer> {

    List<ResearchAction> findAllByState(ActionState state);

}
