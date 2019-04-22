package com.withergate.api.controller;

import com.withergate.api.service.AdminService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class ApplicationController {

    @Value("${info.build.version}")
    private String buildVersion;

    private final AdminService adminService;

    public ApplicationController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Returns the current application version.
     *
     * @return the application version
     */
    @GetMapping("/version")
    public ResponseEntity<String> getVerison() {
        return new ResponseEntity<>(buildVersion, HttpStatus.OK);
    }

    /**
     * Restarts the game. Drops all game data and starts over. Only accessible by admins.
     *
     * @return ok status if applied
     */
    @PostMapping("/restart")
    public ResponseEntity<Void> restartGame() {
        adminService.restartGame();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Ends the current turn. Only accessible by admins.
     *
     * @return ok status if applied
     */
    @PostMapping("/turn/end")
    public ResponseEntity<Void> endTurn() {
        adminService.endTurn();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
