package com.withergate.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/version")
    public ResponseEntity<String> getVerison() {
        return new ResponseEntity<>(buildVersion, HttpStatus.OK);
    }
}
