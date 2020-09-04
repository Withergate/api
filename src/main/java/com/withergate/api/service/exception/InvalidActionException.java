package com.withergate.api.service.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * InvalidAction exception. Used when requested action cannot be performed.
 *
 * @author Martin Myslik
 */
@Getter
public class InvalidActionException extends Exception {

    private final String textId;

    public InvalidActionException(String textId, String message) {
        super(message);
        this.textId = textId;
    }
}
