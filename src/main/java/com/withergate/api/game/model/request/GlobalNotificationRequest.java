package com.withergate.api.game.model.request;

import java.util.Map;

import com.withergate.api.game.model.notification.GlobalNotification.Type;
import com.withergate.api.game.model.notification.LocalizedText;
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

    private Map<String, LocalizedText> message;
    private Type type;
    private boolean active;
    private String link;
    private String linkText;

}
