package com.withergate.api.repository.research;

import com.withergate.api.model.research.ResearchDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ResearchDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ResearchDetailsRepository extends JpaRepository<ResearchDetails, ResearchDetails.ResearchName> {

    List<ResearchDetails> findAllByInformationLevel(int informationLevel);

}
