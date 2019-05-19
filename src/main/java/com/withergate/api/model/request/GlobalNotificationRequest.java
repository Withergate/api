package com.withergate.api.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Global notification request. Used by admins.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class GlobalNotificationRequest {

    private String message;
    private boolean active;

}
