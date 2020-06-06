package com.withergate.api.game.repository.action;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.RestingAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RestingAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface RestingActionRepository extends JpaRepository<RestingAction, Integer> {

    List<RestingAction> findAllByState(ActionState state);

}
