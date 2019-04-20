package com.withergate.api.repository.action;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResourceTradeAction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ResourceTradeAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ResourceTradeActionRepository extends JpaRepository<ResourceTradeAction, Integer> {

    List<ResourceTradeAction> findAllByState(ActionState state);

}
