package com.withergate.api.service;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Admin service.
 *
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final Flyway flyway;
    private final TurnRepository turnRepository;

    public AdminServiceImpl(Flyway flyway, TurnRepository turnRepository) {
        this.flyway = flyway;
        this.turnRepository = turnRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
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
}
