package com.withergate.api.scheduler;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.scheduling.ActionRegistrar;
import com.withergate.api.scheduling.TurnScheduler;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.profile.AchievementService;
import com.withergate.api.service.profile.HistoricalResultsService;
import com.withergate.api.service.profile.ProfileService;
import com.withergate.api.service.turn.TurnService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class TurnSchedulerTest {

    private TurnScheduler scheduler;

    @Mock
    private TurnService turnService;

    @Mock
    private ActionService actionService;

    @Mock
    private ClanService clanService;

    @Mock
    private CharacterService characterService;

    @Mock
    private HistoricalResultsService resultsService;

    @Mock
    private ProfileService profileService;

    @Mock
    private AchievementService achievementService;

    @Mock
    private ActionRegistrar actionRegistrar;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setMaxTurns(45);

        scheduler = new TurnScheduler(turnService, actionService, clanService, characterService, resultsService,
                profileService, achievementService, actionRegistrar, properties);
    }

    @Test
    public void testGivenTurnWhenProcessingCurrentTurnThenVerifyNewTurnSaved() {
        // given turn
        Turn turn = new Turn();
        turn.setTurnId(1);

        Mockito.when(turnService.getCurrentTurn()).thenReturn(turn);

        // when processing current turn
        scheduler.processTurn();

        // then verify new turn saved
        ArgumentCaptor<Turn> captor = ArgumentCaptor.forClass(Turn.class);

        Mockito.verify(turnService).saveTurn(captor.capture());
        assertEquals(2, captor.getValue().getTurnId());
    }

    @Test
    public void testGivenTurnWhenProcessingCurrentTurnThenVerifyAllActionsTriggered() {
        // given turn
        Turn turn = new Turn();
        turn.setTurnId(1);

        Mockito.when(turnService.getCurrentTurn()).thenReturn(turn);

        // when processing current turn
        scheduler.processTurn();

        // then verify all actions triggered
        Mockito.verify(actionService).assignDefaultActions();
        Mockito.verify(actionRegistrar).runActions(1);
        Mockito.verify(clanService).performClanTurnUpdates(1);
    }

    @Test
    public void testGivenTurnWhenMaxTurnsExceededThenVerifyNoActionsPerformed() {
        // given turn
        Turn turn = new Turn();
        turn.setTurnId(46);

        Mockito.when(turnService.getCurrentTurn()).thenReturn(turn);

        // when processing current turn
        scheduler.processTurn();

        // then verify all actions triggered
        Mockito.verify(actionService, Mockito.never()).assignDefaultActions();
        Mockito.verify(actionRegistrar, Mockito.never()).runActions(1);
        Mockito.verify(clanService, Mockito.never()).performClanTurnUpdates(1);
        Mockito.verify(turnService, Mockito.never()).saveTurn(Mockito.any());
    }

}
