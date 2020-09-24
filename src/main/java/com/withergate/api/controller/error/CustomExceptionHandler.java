package com.withergate.api.controller.error;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.withergate.api.game.model.notification.LocalizedText;
import com.withergate.api.game.model.notification.PlaceholderText;
import com.withergate.api.game.repository.notification.PlaceholderTextRepository;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom exception handler. Used for the specification of error codes and messages for exception thrown in
 * controllers.
 *
 * @author Martin Myslik
 */
@Component
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final PlaceholderTextRepository placeholderTextRepository;

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
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false),
                getLocalizedExceptionText(ex));

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
    ResponseEntity<ErrorDetails> handleEntityConflictException(EntityConflictException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), null);

        return new ResponseEntity<>(details, HttpStatus.CONFLICT);
    }

    /**
     * Handles all validation exceptions as bad requests.
     *
     * @param ex the exception
     * @param request the request
     * @return modified response with changed http status
     */
    @ExceptionHandler(ValidationException.class)
    public @ResponseBody
    ResponseEntity<ErrorDetails> handleValidationException(ValidationException ex, WebRequest request) {
        ErrorDetails details = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false), null);

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    private Map<String, LocalizedText> getLocalizedExceptionText(InvalidActionException exception) {
        if (exception.getTextId() == null) {
            return Collections.emptyMap();
        }

        Map<String, LocalizedText> textMap = new HashMap<>();
        for (PlaceholderText placeholderText : placeholderTextRepository.findAllByCode(exception.getTextId())) {
            LocalizedText localizedText = new LocalizedText();
            localizedText.setLang(placeholderText.getLang());
            localizedText.setText(placeholderText.getText());
            textMap.put(placeholderText.getLang(), localizedText);
        }

        return textMap;
    }
}
