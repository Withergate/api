package com.withergate.api.controller.error;

import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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

    /**
     * Handles all invalid action exceptions as bad requests.
     *
     * @param ex the exception
     * @param request the request
     * @return modified response with changed http status
     */
    @ExceptionHandler(InvalidActionException.class)
    public @ResponseBody
    ResponseEntity<ErrorDetails> handleInvalidActionException(InvalidActionException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all entity conflict exceptions as conflicts.
     *
     * @param ex the exception
     * @param request the request
     * @return modified response with changed http status
     */
    @ExceptionHandler(EntityConflictException.class)
    public @ResponseBody
    ResponseEntity<ErrorDetails> handleEntityConflictException(InvalidActionException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(details, HttpStatus.CONFLICT);
    }
}
