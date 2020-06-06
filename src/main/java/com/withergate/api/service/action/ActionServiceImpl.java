package com.withergate.api.service.action;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.LocationAction.LocationActionType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.disaster.DisasterSolution;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.request.DisasterRequest;
import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.disaster.DisasterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.faction.FactionService;
import com.withergate.api.service.location.LocationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Action service. Responsible for the execution of all game actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class ActionServiceImpl implements ActionService {

    private final CharacterService characterService;
    private final LocationService locationService;
    private final DisasterService disasterService;
    private final FactionService factionService;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Retryable
    @Override
    public void performFactionFameDistribution(int turnId, List<Integer> turns) {
        log.debug("-> Checking faction fame distribution...");

        for (int turn : turns) {
            if (turn == turnId) {
                factionService.handleFameDistribution(turnId);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void assignDefaultActions() {
        log.debug("Assigning default actions");

        for (Character character : characterService.loadAll()) {
            // skip characters without clan
            if (character.getClan() == null) {
                continue;
            }

            // only applicable to ready characters
            if (!character.getState().equals(CharacterState.READY)) {
                continue;
            }

            // handle disaster first
            if (character.getClan().isPreferDisaster()) {
                Disaster disaster = disasterService.getDisasterForClan(character.getClan().getId());

                if (disaster != null && character.getClan().getDisasterProgress() < 100) {
                    Optional<DisasterSolution> solution = disaster.getDetails().getSolutions().stream().filter(DisasterSolution::isBasic)
                            .findFirst();
                    if (solution.isPresent()) {
                        DisasterRequest request = new DisasterRequest();
                        request.setCharacterId(character.getId());
                        request.setSolution(solution.get().getIdentifier());
                        try {
                            disasterService.saveAction(request, character.getClan().getId());
                            continue;
                        } catch (InvalidActionException e) {
                            log.error("Error assigning default action.", e);
                        }
                    }
                }
            }

            // exploration
            if (character.getClan().getDefaultAction().equals(Clan.DefaultAction.EXPLORE_NEIGHBORHOOD)) {
                LocationRequest request = new LocationRequest();
                request.setLocation(Location.NEIGHBORHOOD);
                request.setType(LocationActionType.VISIT);
                request.setCharacterId(character.getId());
                try {
                    locationService.saveAction(request, character.getClan().getId());
                } catch (InvalidActionException e) {
                    log.error("Error assigning default action.", e);
                }
            } else {
                character.setState(CharacterState.RESTING);
            }
        }
    }

}
