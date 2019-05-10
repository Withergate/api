package com.withergate.api.repository.action;

import java.util.List;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.MarketTradeAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MarketTradeAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface MarketTradeActionRepository extends JpaRepository<MarketTradeAction, Integer> {

    List<MarketTradeAction> findAllByState(ActionState state);

}
