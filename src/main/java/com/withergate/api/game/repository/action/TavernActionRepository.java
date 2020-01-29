package com.withergate.api.game.repository.action;

import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.TavernAction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TavernAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface TavernActionRepository extends JpaRepository<TavernAction, Integer> {

    List<TavernAction> findAllByState(ActionState state);

}
