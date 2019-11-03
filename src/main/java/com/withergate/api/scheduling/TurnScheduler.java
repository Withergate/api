package com.withergate.api.scheduling;

import com.withergate.api.GameProperties;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
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
    private final ClanService clanService;
    private final GameProperties gameProperties;

    /**
     * Processes all turn-related events at specified times.
     */
    @Scheduled(cron = "0 0 ${game.turnTimes} * * *", zone = "UTC") // every day at specified UTC hours
    public void processTurn() {

        // process current turn
        Turn currentTurn = turnRepository.findFirstByOrderByTurnIdDesc();

        if (currentTurn.getTurnId() > gameProperties.getMaxTurns()) {
            log.info("The game has already ended.");
            return;
        }

        log.info(" === Processing turn: {} ===", currentTurn.getTurnId());

        // assign default actions
        actionService.assignDefaultActions();

        // process location actions
        actionService.processLocationActions(currentTurn.getTurnId());

        // process building actions
        actionService.processBuildingActions(currentTurn.getTurnId());

        // process research actions
        actionService.processResearchActions(currentTurn.getTurnId());

        // process quest actions
        actionService.processQuestActions(currentTurn.getTurnId());

        // process trade actions
        actionService.processTradeActions(currentTurn.getTurnId());

        // handle disaster
        actionService.processDisaster(currentTurn.getTurnId());

        // perform clan turn updates
        clanService.performClanTurnUpdates(currentTurn.getTurnId());

        // prepare next turn
        log.info(" === Finished processing turn: {} ===", currentTurn.getTurnId());

        Turn nextTurn = new Turn();
        nextTurn.setTurnId(currentTurn.getTurnId() + 1);
        turnRepository.save(nextTurn);
    }

}
