package com.withergate.api.controller;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.TurnRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Turn controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class NotificationController {

    private final ClanNotificationRepository clanNotificationRepository;
    private final TurnRepository turnRepository;

    /**
     * Constructor.
     *
     * @param clanNotificationRepository player notification repository
     * @param turnRepository turn repository
     */
    public NotificationController(ClanNotificationRepository clanNotificationRepository,
                                  TurnRepository turnRepository) {
        this.clanNotificationRepository = clanNotificationRepository;
        this.turnRepository = turnRepository;
    }

    /**
     * Retrieves the current notifications for the specified player.
     *
     * @param principal the principal
     * @return the retrieved list of notifications
     */
    @RequestMapping("/notifications/current")
    public ResponseEntity<List<ClanNotification>> getCurrentPlayerNotifications(Principal principal) {

        log.debug("Requesting notifications for player {}", principal.getName());

        int playerId = Integer.parseInt(principal.getName());
        int turn = turnRepository.findFirstByOrderByTurnIdDesc().getTurnId() -1; // get last turn's ID
        return new ResponseEntity<>(clanNotificationRepository.findAllByClanIdAndTurnId(playerId, turn),
                    HttpStatus.OK);
    }

    /**
     * Retrieves all notifications for the specified player.
     *
     * @param principal the principal
     * @return the retrieved list of notifications
     */
    @RequestMapping("/notifications")
    public ResponseEntity<List<ClanNotification>> getAllPlayerNotifications(
            Principal principal, @RequestParam(name = "turn", required = false) Integer turn) {

        log.debug("Requesting notifications for player {}", principal.getName());

        int playerId = Integer.parseInt(principal.getName());
        return new ResponseEntity<>(clanNotificationRepository.findAllByClanId(playerId), HttpStatus.OK);
    }
}
