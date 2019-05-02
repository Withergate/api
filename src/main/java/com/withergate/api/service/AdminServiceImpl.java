package com.withergate.api.service;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.scheduling.TurnScheduler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

/**
 * Admin service.
 *
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final Flyway flyway;
    private final TurnRepository turnRepository;
    private final TurnScheduler turnScheduler;

    @Override
    public void restartGame() {
        log.info("Restart request accepted.");

        // clean the database
        flyway.clean();
        flyway.migrate();

        // create first turn
        Turn turn = new Turn();
        turn.setTurnId(1);
        turnRepository.save(turn);

        log.info("Game restarted.");
    }

    @Override
    public void endTurn() {
        log.info("End turn requested manually.");

        turnScheduler.processTurn();
    }
}
