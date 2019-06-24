package com.withergate.api.service.disaster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.model.Clan;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterDetails;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.repository.disaster.DisasterDetailsRepository;
import com.withergate.api.repository.disaster.DisasterRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.ClanService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Disaster service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class DisasterServiceImpl implements DisasterService {

    // turns when disasters will be triggered
    private static final int[] DISASTER_TURNS = new int[]{15, 30, 45, 60};

    // how many turns in advance will the disaster be known
    private static final int DISASTER_VISIBILITY = 10;

    private final DisasterRepository disasterRepository;
    private final DisasterDetailsRepository disasterDetailsRepository;
    private final ClanService clanService;
    private final TurnRepository turnRepository;
    private final RandomService randomService;

    @Override
    public Disaster getDisasterForClan(int clanId) {
        Clan clan = clanService.getClan(clanId);

        // get the current disaster
        Disaster disaster = disasterRepository.findFirstByCompleted(false);

        // get current turn
        Turn turn = turnRepository.findFirstByOrderByTurnIdDesc();

        // no disaster to show
        if (disaster == null) {
            return null;
        }

        // check if clan knows about this disaster
        boolean isVisible = false;
        if ((disaster.getTurn() - DISASTER_VISIBILITY - clan.getInformationLevel()) >= turn.getTurnId()) {
            isVisible = true;
        }

        return isVisible ? disaster : null;
    }

    @Override
    public void handleDisaster(int turnId) {
        log.debug("Checking current disaster");

        // get the current disaster
        Disaster disaster = disasterRepository.findFirstByCompleted(false);

        // check disaster
        if (disaster != null) {
            // check trigger
            if (disaster.getTurn() < turnId) {
                log.debug("Existing disaster not triggering this turn.");
            } else {
                triggerDisaster(turnId, disaster);
                prepareNextDisaster(turnId);
            }
        } else {
            prepareNextDisaster(turnId);
        }
    }

    private void triggerDisaster(int turnId, Disaster disaster) {
        log.debug("Triggering disaster: {}", disaster.getDetails().getIdentifier());

        // not implemented yet
    }


    private void prepareNextDisaster(int turnId) {
        log.debug("Preparing next disaster.");

        List<Disaster> previousDisasters = disasterRepository.findAll();
        Set<String> identifiers = new HashSet<>();
        for (Disaster disaster : previousDisasters) {
            identifiers.add(disaster.getDetails().getIdentifier());
        }

        List<DisasterDetails> detailsList = disasterDetailsRepository.findAll()
                // filter out all previous disasters
                .stream().filter(d -> !identifiers.contains(d.getIdentifier()))
                .collect(Collectors.toList());

        if (detailsList.size() == 0) {
            log.error("No new disasters found.");

            return;
        }

        // prepare disaster
        DisasterDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));

        Disaster disaster = new Disaster();
        disaster.setDetails(details);
        disaster.setCompleted(false);

        // get the lowest possible turn for the next disaster
        for (int turn : DISASTER_TURNS) {
            if (turnId < turn) {
                disaster.setTurn(turn);
                break;
            }
        }

        log.debug("Disaster {} prepared for turn {}.", disaster.getDetails().getIdentifier(), disaster.getTurn());

        disasterRepository.save(disaster);
    }

}
