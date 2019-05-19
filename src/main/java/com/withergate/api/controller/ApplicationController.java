package com.withergate.api.controller;

import com.withergate.api.model.notification.GlobalNotification;
import com.withergate.api.model.request.GlobalNotificationRequest;
import com.withergate.api.repository.notification.GlobalNotificationRepository;
import com.withergate.api.service.AdminService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final GlobalNotificationRepository globalNotificationRepository;

    public ApplicationController(AdminService adminService,
                                 GlobalNotificationRepository globalNotificationRepository) {
        this.adminService = adminService;
        this.globalNotificationRepository = globalNotificationRepository;
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

    /**
     * Updates global notificaiton. Only accessible by admins.
     *
     * @return ok status if applied
     */
    @PutMapping("/notifications/global")
    public ResponseEntity<Void> updateGlobalNotification(@RequestBody GlobalNotificationRequest request) {
        adminService.updateGlobalNotification(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns the current global notification.
     *
     * @return the application version
     */
    @GetMapping("/notifications/global")
    public ResponseEntity<GlobalNotification> getGlobalNotification() {
        return new ResponseEntity<>(globalNotificationRepository.getOne(GlobalNotification.Singleton.SINGLE), HttpStatus.OK);
    }
}
