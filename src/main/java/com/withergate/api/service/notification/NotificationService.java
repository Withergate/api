package com.withergate.api.service.notification;

import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.LocalizedText;

import java.util.Map;

/**
 * Notification service interface.
 *
 * @author Martin Myslik
 */
public interface NotificationService {

    /**
     * Saves the notification to database
     *
     * @param notification the notification
     * @return the saved notification
     */
    ClanNotification save(ClanNotification notification);

    /**
     * Constructs localized texts from code and supplied values and adds to the map of existing localized texts. If
     * an entry exists for given language, the new entry will be appended to the end of it.
     *
     * @param texts  the existing map of texts
     * @param code   the code of the entry
     * @param values the values to be injected to the text
     */
    void addLocalizedTexts(Map<String, LocalizedText> texts, String code, String[] values);

    /**
     * Constructs localized texts from code and supplied values and adds to the map of existing localized texts. If
     * an entry exists for given language, the new entry will be appended to the end of it.
     *
     * @param texts  the existing map of texts
     * @param code   the code of the entry
     * @param values the values to be injected to the text
     * @param injects the values to be injected from already translated texts
     */
    void addLocalizedTexts(Map<String, LocalizedText> texts, String code, String[] values, Map<String, LocalizedText> injects);
}
