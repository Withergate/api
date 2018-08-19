package com.withergate.api.service.exception;

/**
 * InvalidAction exception. Used when requested action cannot be performed.
 *
 * @author Martin Myslik
 */
public class InvalidActionException extends Exception {

    public InvalidActionException(String message) {
        super(message);
    }
}
