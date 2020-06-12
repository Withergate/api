package com.withergate.api.service.action;

import java.util.List;
import java.util.Optional;

import com.withergate.api.game.model.action.LocationAction.LocationActionType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.DefaultAction;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.disaster.DisasterSolution;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.request.DisasterRequest;
import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.game.model.request.RestingRequest;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.RestingService;
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
    private final RestingService restingService;

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

            try {
                createDefaultAction(character);
                character.setState(CharacterState.BUSY);
            } catch (InvalidActionException e) {
                log.error("Error assigning default action to character {}.", character.getId(), e);
            }
        }
    }

    private void createDefaultAction(Character character) throws InvalidActionException {
        if (character.isPreferDisaster()) {
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
                        return; // do not continue
                    } catch (InvalidActionException e) {
                        log.error("Error assigning default action.", e);
                    }
                }
            }
        }

        switch (character.getDefaultAction()) {
            case REST: {
                RestingRequest request = new RestingRequest();
                request.setCharacterId(character.getId());
                restingService.saveAction(request, character.getClan().getId());
                break;
            }
            case EXPLORE_NEIGHBORHOOD: {
                LocationRequest request = new LocationRequest();
                request.setLocation(Location.NEIGHBORHOOD);
                request.setType(LocationActionType.VISIT);
                request.setCharacterId(character.getId());
                locationService.saveAction(request, character.getClan().getId());
                break;
            }
            case SCOUT_WASTELAND: {
                LocationRequest request = new LocationRequest();
                request.setLocation(Location.WASTELAND);
                request.setType(LocationActionType.SCOUT);
                request.setCharacterId(character.getId());
                locationService.saveAction(request, character.getClan().getId());
                break;
            }
            default:
                log.error("Unknown default action type: {}.", character.getDefaultAction());
        }
    }

}
