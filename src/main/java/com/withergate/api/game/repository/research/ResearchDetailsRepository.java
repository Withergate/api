package com.withergate.api.game.repository.research;

import com.withergate.api.game.model.research.ResearchDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ResearchDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ResearchDetailsRepository extends JpaRepository<ResearchDetails, String> {

}
