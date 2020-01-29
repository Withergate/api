package com.withergate.api.game.repository.disaster;

import com.withergate.api.game.model.disaster.DisasterSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DisasterSolution repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface DisasterSolutionRepository extends JpaRepository<DisasterSolution, String> {

}
