package com.withergate.api.controller.notification;

import java.security.Principal;
import java.util.List;

import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.repository.notification.ClanNotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Turn controller.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@RestController
public class NotificationController {

    private final ClanNotificationRepository clanNotificationRepository;
    private final TurnRepository turnRepository;

    /**
     * Retrieves all notifications for the specified player.
     *
     * @param principal the principal
     * @return the retrieved list of notifications
     */
    @GetMapping("/notifications")
    public ResponseEntity<List<ClanNotification>> getPlayerNotificationsForTurn(
            Principal principal, @RequestParam(name = "turn", required = false) Integer turn) {

        if (turn == null) {
            // default to last turn
            turn = turnRepository.findFirstByOrderByTurnIdDesc().getTurnId() - 1;
        }

        int playerId = Integer.parseInt(principal.getName());
        return new ResponseEntity<>(clanNotificationRepository.findAllByClanIdAndTurnId(playerId, turn),
                HttpStatus.OK);
    }
}
