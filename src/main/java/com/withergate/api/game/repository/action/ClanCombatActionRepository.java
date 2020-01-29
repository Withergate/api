package com.withergate.api.game.repository.action;

import java.util.List;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ClanCombatAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ArenaAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ClanCombatActionRepository extends JpaRepository<ClanCombatAction, Integer> {

    List<ClanCombatAction> findAllByState(ActionState state);

}
