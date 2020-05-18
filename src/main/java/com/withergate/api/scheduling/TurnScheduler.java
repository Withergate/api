package com.withergate.api.scheduling;

import java.time.LocalDate;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.profile.AchievementService;
import com.withergate.api.service.profile.HistoricalResultsService;
import com.withergate.api.service.profile.ProfileService;
import com.withergate.api.service.turn.TurnService;
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

    private final TurnService turnService;
    private final ActionService actionService;
    private final ClanService clanService;
    private final CharacterService characterService;
    private final HistoricalResultsService resultsService;
    private final ProfileService profileService;
    private final AchievementService achievementService;
    private final GameProperties gameProperties;

    /**
     * Processes all turn-related events at specified times.
     */
    @Scheduled(cron = "0 0 ${game.turnTimes} * * *", zone = "UTC") // every day at specified UTC hours
    public void processTurn() {
        // process current turn
        Turn currentTurn = turnService.getCurrentTurn();

        if (currentTurn.getTurnId() > gameProperties.getMaxTurns()) {
            log.info("The game has already ended.");
            return;
        }
        LocalDate currentDate = LocalDate.now();
        if (currentTurn.getStartDate() != null && currentDate.isBefore(currentTurn.getStartDate())) {
            log.info("Current turn has not started yet.");
            return;
        }

        // process turn
        processTurn(currentTurn);

        Turn nextTurn = new Turn();
        nextTurn.setTurnId(currentTurn.getTurnId() + 1);
        turnService.saveTurn(nextTurn);
    }

    private void processTurn(Turn currentTurn) {
        log.info(" === Processing turn: {} ===", currentTurn.getTurnId());

        // assign default actions
        actionService.assignDefaultActions();

        // process clan combat actions
        actionService.processClanCombatActions(currentTurn.getTurnId());

        // process research actions
        actionService.processResearchActions(currentTurn.getTurnId());

        // process building actions
        actionService.processBuildingActions(currentTurn.getTurnId());

        // process building actions
        actionService.processCraftingActions(currentTurn.getTurnId());

        // process location actions
        actionService.processLocationActions(currentTurn.getTurnId());

        // process quest actions
        actionService.processQuestActions(currentTurn.getTurnId());

        // process trade actions
        actionService.processTradeActions(currentTurn.getTurnId());

        // process faction actions
        actionService.processFactionActions(currentTurn.getTurnId());

        // handle disaster
        actionService.processDisaster(currentTurn.getTurnId());

        // delete dead characters
        characterService.deleteDeadCharacters();

        // perform clan turn updates
        clanService.performClanTurnUpdates(currentTurn.getTurnId());
        characterService.deleteDeadCharacters();

        // perform faction fame distribution
        actionService.performFactionFameDistribution(currentTurn.getTurnId(), gameProperties.getFactionTurns());

        // perform end game actions
        if (currentTurn.getTurnId() == gameProperties.getMaxTurns()) {
            // faction achievements
            achievementService.handleEndGameFactionAchievements();

            // save historical results
            resultsService.saveResults(clanService.getAllClansByFame());

            // recalcalculate rankings
            profileService.recalculateRankings();

            // end game achievements
            achievementService.handleEndGameProfileAchievements();
        }

        // prepare statistics
        clanService.prepareStatistics(currentTurn.getTurnId());

        // handle achievements
        achievementService.handleEndTurnAchievements();

        // prepare next turn
        log.info(" === Finished processing turn: {} ===", currentTurn.getTurnId());
    }

}
