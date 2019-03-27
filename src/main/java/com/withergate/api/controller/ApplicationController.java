package com.withergate.api.controller;

import com.withergate.api.service.AdminService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping("/version")
    public ResponseEntity<String> getVerison() {
        return new ResponseEntity<>(buildVersion, HttpStatus.OK);
    }

    /**
     * Restarts the game. Drops all game data and starts over. Only accessible by administators.
     *
     * @return ok status if applied
     */
    @RequestMapping(value = "/restart", method = RequestMethod.POST)
    public ResponseEntity<Void> restartGame() {
        adminService.restartGame();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
