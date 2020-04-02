package com.withergate.api.game.repository.action;

import java.util.List;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ArenaAction;
import com.withergate.api.game.model.action.CraftingAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CraftingAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface CraftingActionRepository extends JpaRepository<CraftingAction, Integer> {

    List<CraftingAction> findAllByState(ActionState state);

}
