package com.withergate.api.service.game;

import java.time.LocalDate;

import com.withergate.api.game.model.request.GlobalNotificationRequest;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Admin service interface.
 *
 * @author Martin Myslik
 */
public interface AdminService {

    /**
     * Restarts the whole game.
     */
    @PreAuthorize("hasRole('ADMIN')")
    void restartGame();

    /**
     * Forces the current turn to end.
     */
    @PreAuthorize("hasRole('ADMIN')")
    void endTurn();

    /**
     * Updates global notification.
     *
     * @param request notification payload
     */
    @PreAuthorize("hasRole('ADMIN')")
    void updateGlobalNotification(GlobalNotificationRequest request);

    /**
     * Sets a start date for the current turn.
     *
     * @param date start date
     */
    @PreAuthorize("hasRole('ADMIN')")
    void setTurnStartDate(LocalDate date);

}
