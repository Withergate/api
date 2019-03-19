package com.withergate.api.scheduling;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.clan.CharacterService;

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
    private final ActionService actionService;
    private final CharacterService characterService;

    /**
     * Constructor.
     *
     * @param turnRepository   turn repository
     * @param actionService    locationAction service
     * @param characterService character service
     */
    public TurnScheduler(TurnRepository turnRepository, ActionService actionService,
                         CharacterService characterService) {
        this.turnRepository = turnRepository;
        this.actionService = actionService;
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

        // process arena actions
        actionService.performPendingArenaActions(currentTurn.getTurnId());

        // process location actions
        actionService.performPendingLocationActions(currentTurn.getTurnId());

        // process building actions
        actionService.performPendingBuildingActions(currentTurn.getTurnId());

        // process leveling up
        characterService.performCharacterLeveling(currentTurn.getTurnId());

        /**
         * Prepare next turn data.
         */
        log.info("Turn finished - preparing next turn.");

        Turn nextTurn = new Turn();
        nextTurn.setTurnId(currentTurn.getTurnId() + 1);
        turnRepository.save(nextTurn);
    }

}
