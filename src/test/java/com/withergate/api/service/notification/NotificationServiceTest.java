package com.withergate.api.service.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.LocalizedText;
import com.withergate.api.model.notification.PlaceholderText;
import com.withergate.api.repository.notification.ClanNotificationRepository;
import com.withergate.api.repository.notification.PlaceholderTextRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class NotificationServiceTest {

    private NotificationServiceImpl notificationService;

    @Mock
    private ClanNotificationRepository notificationRepository;

    @Mock
    private PlaceholderTextRepository placeholderTextRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        notificationService = new NotificationServiceImpl(notificationRepository, placeholderTextRepository);
    }

    @Test
    public void testGivenNotificationWhenSavingThenVerifyNotificationSaved() {
        // given notification
        ClanNotification notification = new ClanNotification();
        notification.setClanId(1);
        notification.setTurnId(1);

        // when saving
        notificationService.save(notification);

        // then verify notification saved
        Mockito.verify(notificationRepository).save(notification);
    }

    @Test
    public void testGivenTextWithPlaceholdersWhenAddingLocalizedTextsThenVerifyTextGenerated() {
        // given text with placeholders
        String code = "test";
        PlaceholderText text = new PlaceholderText();
        text.setCode("test");
        text.setLang("en");
        text.setText("{} and {} - [].");
        List<PlaceholderText> texts = new ArrayList<>();
        texts.add(text);
        Mockito.when(placeholderTextRepository.findAllByCode(code)).thenReturn(texts);

        // when adding localized texts
        ClanNotification notification = new ClanNotification(1, 1);
        Map<String, LocalizedText> injects = new HashMap<>();
        LocalizedText inject = new LocalizedText();
        inject.setLang("en");
        inject.setText("Inject");
        injects.put("en", inject);

        notificationService.addLocalizedTexts(notification.getText(), code, new String[]{"One", "Two"}, injects);

        // then verify correct text constructed
        Assert.assertEquals("One and Two - Inject.", notification.getText().get("en").getText());
    }

}
