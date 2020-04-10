package com.withergate.api.service.game;

import java.util.HashMap;
import java.util.Map;

import com.withergate.api.game.model.notification.GlobalNotification;
import com.withergate.api.game.model.notification.GlobalNotification.Type;
import com.withergate.api.game.model.notification.LocalizedText;
import com.withergate.api.game.model.request.GlobalNotificationRequest;
import com.withergate.api.game.repository.notification.GlobalNotificationRepository;
import com.withergate.api.scheduling.TurnScheduler;
import com.withergate.api.service.turn.TurnService;
import org.flywaydb.core.Flyway;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AdminServiceTest {

    private AdminServiceImpl adminService;

    @Mock
    private Flyway flyway;

    @Mock
    private TurnService turnService;

    @Mock
    private TurnScheduler turnScheduler;

    @Mock
    private GlobalNotificationRepository globalNotificationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        adminService = new AdminServiceImpl(flyway, turnService, turnScheduler, globalNotificationRepository);
    }

    @Test
    public void testGivenServiceWhenManuallyEndingTurnThenVerifySchedulerCalled() {
        // given service when manually ending turn
        adminService.endTurn();

        // then verify scheduler called
        Mockito.verify(turnScheduler).processTurn();
    }

    @Test
    public void testGivenNotificationRequestWhenUpdatingGlobalNotificationThenVerifyEntityUpdated() {
        // given request
        GlobalNotificationRequest request = new GlobalNotificationRequest();
        request.setActive(true);
        request.setType(Type.GLOBAL);

        Map<String, LocalizedText> messageRequest = new HashMap<>();
        LocalizedText text = new LocalizedText();
        text.setText("Test");
        text.setLang("en");
        messageRequest.put("en", text);
        request.setMessage(messageRequest);

        GlobalNotification notification = new GlobalNotification();
        notification.setActive(false);
        Map<String, LocalizedText> messageNotification = new HashMap<>();
        LocalizedText text2 = new LocalizedText();
        text2.setText("");
        text2.setLang("en");
        messageNotification.put("en", text2);
        notification.setMessage(messageNotification);
        Mockito.when(globalNotificationRepository.getOne(Type.GLOBAL)).thenReturn(notification);

        // when updating
        adminService.updateGlobalNotification(request);

        // then verify entity updated
        Assert.assertEquals("Test", notification.getMessage().get("en").getText());
        Assert.assertTrue(notification.isActive());
    }

}
