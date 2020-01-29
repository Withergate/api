package com.withergate.api.game.repository.disaster;

import com.withergate.api.game.model.disaster.Disaster;
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
