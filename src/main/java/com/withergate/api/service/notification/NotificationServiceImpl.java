package com.withergate.api.service.notification;

import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.LocalizedText;
import com.withergate.api.model.notification.PlaceholderText;
import com.withergate.api.repository.notification.ClanNotificationRepository;
import com.withergate.api.repository.notification.PlaceholderTextRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Notification service. Used for the construction of dynamic localized notifications.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final ClanNotificationRepository notificationRepository;
    private final PlaceholderTextRepository placeholderTextRepository;

    public NotificationServiceImpl(ClanNotificationRepository notificationRepository,
                                   PlaceholderTextRepository placeholderTextRepository) {
        this.notificationRepository = notificationRepository;
        this.placeholderTextRepository = placeholderTextRepository;
    }

    @Override
    public ClanNotification save(ClanNotification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void addLocalizedTexts(Map<String, LocalizedText> texts, String code, String[] values) {
        addLocalizedTexts(texts, code, values, new HashMap<>());
    }

    @Override
    public void addLocalizedTexts(Map<String, LocalizedText> texts, String code, String[] values, Map<String, LocalizedText> injects) {
        List<PlaceholderText> placeholders = placeholderTextRepository.findAllByCode(code);
        if (placeholders.isEmpty()) log.error("Error loading placeholder texts for {}.", code);

        for (PlaceholderText text : placeholderTextRepository.findAllByCode(code)) {
            String enhancedText = enhanceText(text.getText(), values);
            enhancedText = enhanceText(enhancedText, text.getLang(), injects);

            // create new entry
            if (!texts.containsKey(text.getLang())) {
                LocalizedText localizedText = new LocalizedText();
                localizedText.setLang(text.getLang());
                localizedText.setText(enhancedText);

                texts.put(text.getLang(), localizedText);
            } else {
                // append to existing
                LocalizedText localizedText = texts.get(text.getLang());
                localizedText.setText(localizedText.getText() + " " + enhancedText);
            }
        }
    }

    private String enhanceText(String text, String[] values) {
        for (String value : values) {
            text = text.replaceFirst("\\{}", value);
        }

        // capitalize first letter
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private String enhanceText(String text, String lang, Map<String, LocalizedText> injects) {
        if (injects.containsKey(lang)) {
            return text.replace("[]", injects.get(lang).getText());
        }

        return text;
    }

}
