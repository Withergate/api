package com.withergate.api.controller.error;

import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Custom exception handler. Used for the specification of error codes and messages for exception thrown in
 * controllers.
 *
 * @author Martin Myslik
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(InvalidActionException.class)
    public final ResponseEntity<ErrorDetails> handleInvalidActionException(InvalidActionException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityConflictException.class)
    public final ResponseEntity<ErrorDetails> handleEntityConflictException(InvalidActionException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(details, HttpStatus.CONFLICT);
    }
}
