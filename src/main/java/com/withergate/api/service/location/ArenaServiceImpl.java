package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ArenaAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.arena.ArenaStats;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.repository.action.ArenaActionRepository;
import com.withergate.api.repository.arena.ArenaStatsRepository;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Arena service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class ArenaServiceImpl implements ArenaService {

    private final ArenaActionRepository arenaActionRepository;
    private final CombatService combatService;
    private final NotificationService notificationService;
    private final ArenaStatsRepository arenaStatsRepository;

    @Override
    public void saveArenaAction(ArenaAction action) {
        arenaActionRepository.save(action);
    }

    @Override
    public void processArenaActions(int turnId) {
        log.debug("Executing arena actions");

        List<ArenaAction> actions = arenaActionRepository.findAllByState(ActionState.PENDING);
        List<Character> characters = new ArrayList<>(actions.size());

        for (ArenaAction action : actions) {
            characters.add(action.getCharacter());

            // mark action as completed
            action.setState(ActionState.COMPLETED);
        }

        log.debug("{} characters entered arena.", characters.size());

        // process arena fights
        Collections.shuffle(characters);
        List<ArenaResult> results = combatService.handleArenaFights(characters);

        for (ArenaResult result : results) {
            // save results
            result.getNotification().setTurnId(turnId);
            result.getNotification().setHeader(result.getCharacter().getName());
            notificationService.save(result.getNotification());

            // handle stats update for winner
            if (result.isWinner()) {
                saveOrUpdateArenaStats(result);
            }
        }
    }

    private void saveOrUpdateArenaStats(ArenaResult result) {
        Optional<ArenaStats> stats = arenaStatsRepository.findById(result.getCharacter().getId());

        if (stats.isPresent()) {
            // update existing
            stats.get().setStats(stats.get().getStats() + 1);
        } else {
            // create new stats
            ArenaStats newStats = new ArenaStats();
            newStats.setId(result.getCharacter().getId());
            newStats.setCharacterName(result.getCharacter().getName());
            newStats.setClanName(result.getCharacter().getClan().getName());
            newStats.setStats(1);

            arenaStatsRepository.save(newStats);
        }
    }
}
