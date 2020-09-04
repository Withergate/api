package com.withergate.api.controller.error;

import com.withergate.api.game.model.notification.LocalizedText;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * Error details object used for error reporting.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;
    private Map<String, LocalizedText> localizedText;

}
