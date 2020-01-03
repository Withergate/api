package com.withergate.api.repository.action;

import java.util.List;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.FactionAction;
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
