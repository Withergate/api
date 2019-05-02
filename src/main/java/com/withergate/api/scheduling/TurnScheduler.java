package com.withergate.api.scheduling;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.clan.CharacterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Turn scheduler. Executes scheduled method every day that handles all game actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Component
public class TurnScheduler {

    private final TurnRepository turnRepository;
    private final ActionService actionService;
    private final CharacterService characterService;

    /**
     * Processes all turn-related events every midnight.
     */
    @Scheduled(cron = "0 0 0 * * *") // every midnight
    public void processTurn() {

        // process current turn
        Turn currentTurn = turnRepository.findFirstByOrderByTurnIdDesc();
        log.info("Processing current turn: {}", currentTurn.getTurnId());

        // process location actions
        actionService.processLocationActions(currentTurn.getTurnId());

        // process building actions
        actionService.processBuildingActions(currentTurn.getTurnId());

        // process quest actions
        actionService.processQuestActions(currentTurn.getTurnId());

        // process trade actions
        actionService.processTradeActions(currentTurn.getTurnId());

        // update characters
        characterService.performCharacterTurnUpdates(currentTurn.getTurnId());

        // prepare next turn
        log.info("Turn finished - preparing next turn.");

        Turn nextTurn = new Turn();
        nextTurn.setTurnId(currentTurn.getTurnId() + 1);
        turnRepository.save(nextTurn);
    }

}
