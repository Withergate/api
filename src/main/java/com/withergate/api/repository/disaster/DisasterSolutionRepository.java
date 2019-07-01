package com.withergate.api.repository.disaster;

import com.withergate.api.model.disaster.DisasterSolution;
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
