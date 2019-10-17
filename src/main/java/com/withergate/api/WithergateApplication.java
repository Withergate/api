package com.withergate.api;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class.
 *
 * @author Martin Myslik
 */
@EnableScheduling
@EnableRetry
@SpringBootApplication
public class WithergateApplication {

    /**
     * Main application entry point.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(WithergateApplication.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
    }
}
