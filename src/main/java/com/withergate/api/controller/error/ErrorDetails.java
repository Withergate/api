package com.withergate.api.controller.error;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Error details object used for error reporting.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
