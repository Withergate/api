package com.withergate.api.game.repository.action;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.DisasterAction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DisasterAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface DisasterActionRepository extends JpaRepository<DisasterAction, Integer> {

    List<DisasterAction> findAllByState(ActionState state);

}
