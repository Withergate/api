package com.withergate.api.game.repository.action;

import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.LocationAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LocationAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface LocationActionRepository extends JpaRepository<LocationAction, Integer> {

    List<LocationAction> findAllByState(ActionState state);

    List<LocationAction> findAllByStateAndLocation(ActionState state, Location location);

}
