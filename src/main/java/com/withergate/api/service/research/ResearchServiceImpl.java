package com.withergate.api.service.research;

import com.withergate.api.model.Clan;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails;
import com.withergate.api.repository.research.ResearchDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Research service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ResearchServiceImpl implements ResearchService {

    private final ResearchDetailsRepository detailsRepository;

    @Override
    public void assignResearch(Clan clan) {
        log.debug("Assigning initial research to clan");

        for (ResearchDetails details : detailsRepository.findAllByInformationLevel(clan.getInformationLevel())) {
            Research research = new Research();
            research.setDetails(details);
            research.setClan(clan);
            research.setProgress(0);
            research.setCompleted(false);

            clan.getResearch().put(details.getIdentifier(), research);
        }
    }
}
