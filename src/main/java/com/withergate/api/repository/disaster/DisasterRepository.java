package com.withergate.api.repository.disaster;

import com.withergate.api.model.disaster.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Disaster repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface DisasterRepository extends JpaRepository<Disaster, String> {

    Disaster findFirstByCompleted(boolean completed);

}
