package com.withergate.api.game.repository.action;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ResourceTradeAction;

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
