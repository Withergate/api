package com.withergate.api.service;

import com.withergate.api.repository.TurnRepository;
import com.withergate.api.scheduling.TurnScheduler;
import org.flywaydb.core.Flyway;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        adminService = new AdminServiceImpl(flyway, turnRepository, turnScheduler);
    }

    @Test
    public void testGivenServiceWhenManuallyEndingTurnThenVerifySchedulerCalled() {
        // given service when manually ending turn
        adminService.endTurn();

        // then verify scheduler called
        Mockito.verify(turnScheduler).processTurn();
    }

}
