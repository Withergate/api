package com.withergate.api.service.exception;

/**
 * Validation exception. Used when requested input is not in a correct format.
 *
 * @author Martin Myslik
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
