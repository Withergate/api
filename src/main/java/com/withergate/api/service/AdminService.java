package com.withergate.api.service;

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

}
