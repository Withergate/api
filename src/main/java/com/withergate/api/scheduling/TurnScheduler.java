package com.withergate.api.scheduling;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.service.CharacterService;
import com.withergate.api.service.action.ActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Turn scheduler. Executes scheduled method every day that handles all game actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@Component
public class TurnScheduler {

    private final TurnRepository turnRepository;
    private final ActionService locationActionService;
    private final CharacterService characterService;

    /**
     * Constructor.
     *
     * @param turnRepository turn repository
     * @param locationActionService locationAction service
     * @param characterService character service
     */
    public TurnScheduler(TurnRepository turnRepository, ActionService locationActionService,
                         CharacterService characterService) {
        this.turnRepository = turnRepository;
        this.locationActionService = locationActionService;
        this.characterService = characterService;
    }

    // @Scheduled(cron = "0 0 0 * * *") // every midnight
    @Scheduled(cron = "0 0/2 * * * ?") // testing
    public void processTurn() {
        /**
         * Process current turn.
         */
        Turn currentTurn = turnRepository.findFirstByOrderByTurnIdDesc();
        log.info("Processing current turn: {}", currentTurn.getTurnId());

        // perform character healing
        characterService.performCharacterHealing(currentTurn.getTurnId());

        // process actions
        locationActionService.performPendingLocationActions(currentTurn.getTurnId());

        /**
         * Prepare next turn data.
         */
        log.info("Turn finished - preparing next turn.");

        Turn nextTurn = new Turn();
        nextTurn.setTurnId(currentTurn.getTurnId() + 1);
        turnRepository.save(nextTurn);
    }
}
