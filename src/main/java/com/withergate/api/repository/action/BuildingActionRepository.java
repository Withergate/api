package com.withergate.api.repository.action;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BuildingAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface BuildingActionRepository extends JpaRepository<BuildingAction, Integer> {

    List<BuildingAction> findAllByState(ActionState state);

}
