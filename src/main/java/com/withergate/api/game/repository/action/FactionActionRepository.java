package com.withergate.api.game.repository.action;

import java.util.List;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.FactionAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * FactionAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface FactionActionRepository extends JpaRepository<FactionAction, Integer> {

    List<FactionAction> findAllByState(ActionState state);

}
