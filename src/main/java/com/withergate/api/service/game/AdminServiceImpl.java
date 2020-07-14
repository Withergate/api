package com.withergate.api.service.game;

import com.withergate.api.game.model.notification.GlobalNotification;
import com.withergate.api.game.model.notification.GlobalNotification.Type;
import com.withergate.api.game.model.request.GlobalNotificationRequest;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.game.repository.notification.GlobalNotificationRepository;
import com.withergate.api.scheduling.TurnScheduler;
import com.withergate.api.service.turn.TurnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Admin service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    @Qualifier("flyway")
    private final Flyway flyway;
    private final TurnService turnService;
    private final TurnScheduler turnScheduler;
    private final GlobalNotificationRepository globalNotificationRepository;

    @Override
    public void restartGame() {
        log.info("Restart request accepted.");

        // clean the database
        flyway.clean();
        flyway.migrate();

        // create first turn
        Turn turn = new Turn();
        turn.setTurnId(1);
        turnService.saveTurn(turn);

        log.info("Game restarted.");
    }

    @Override
    public void endTurn() {
        log.info("End turn requested manually.");

        turnScheduler.processTurn();
    }

    @Transactional
    @Override
    public void updateGlobalNotification(GlobalNotificationRequest request) {
        GlobalNotification notification = globalNotificationRepository.getOne(request.getType());
        notification.setActive(request.isActive());
        notification.setLink(request.getLink());
        notification.setLinkText(request.getLinkText());

        for (String lang : request.getMessage().keySet()) {
            if (!notification.getMessage().containsKey(lang)) continue;
            notification.getMessage().get(lang).setText(request.getMessage().get(lang).getText());
        }
    }

    @Transactional
    @Override
    public void setTurnStartDate(LocalDate date) {
        Turn turn = turnService.getCurrentTurn();
        turn.setStartDate(date);
    }
}
