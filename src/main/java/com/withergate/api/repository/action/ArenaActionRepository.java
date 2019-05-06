package com.withergate.api.repository.action;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ArenaAction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ArenaAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ArenaActionRepository extends JpaRepository<ArenaAction, Integer> {

    List<ArenaAction> findAllByState(ActionState state);

}
