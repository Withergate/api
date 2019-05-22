package com.withergate.api.service;

import com.withergate.api.model.notification.GlobalNotification;
import com.withergate.api.model.notification.GlobalNotification.Singleton;
import com.withergate.api.model.request.GlobalNotificationRequest;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.repository.notification.GlobalNotificationRepository;
import com.withergate.api.scheduling.TurnScheduler;
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
    private TurnRepository turnRepository;

    @Mock
    private TurnScheduler turnScheduler;

    @Mock
    private GlobalNotificationRepository globalNotificationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        adminService = new AdminServiceImpl(flyway, turnRepository, turnScheduler, globalNotificationRepository);
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
        request.setMessage("Test");

        GlobalNotification notification = new GlobalNotification();
        notification.setActive(false);
        notification.setMessage("");
        Mockito.when(globalNotificationRepository.getOne(Singleton.SINGLE)).thenReturn(notification);

        // when updating
        adminService.updateGlobalNotification(request);

        // then verify entity updated
        Assert.assertEquals("Test", notification.getMessage());
        Assert.assertTrue(notification.isActive());
    }

}
