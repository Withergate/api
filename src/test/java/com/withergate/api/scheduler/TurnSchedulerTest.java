package com.withergate.api.scheduler;

import com.withergate.api.GameProperties;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.scheduling.TurnScheduler;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
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
    private TurnRepository turnRepository;

    @Mock
    private ActionService actionService;

    @Mock
    private ClanService clanService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setMaxTurns(45);

        scheduler = new TurnScheduler(turnRepository, actionService, clanService, properties);
    }

    @Test
    public void testGivenTurnWhenProcessingCurrentTurnThenVerifyNewTurnSaved() {
        // given turn
        Turn turn = new Turn();
        turn.setTurnId(1);

        Mockito.when(turnRepository.findFirstByOrderByTurnIdDesc()).thenReturn(turn);

        // when processing current turn
        scheduler.processTurn();

        // then verify new turn saved
        ArgumentCaptor<Turn> captor = ArgumentCaptor.forClass(Turn.class);

        Mockito.verify(turnRepository).save(captor.capture());
        assertEquals(2, captor.getValue().getTurnId());
    }

    @Test
    public void testGivenTurnWhenProcessingCurrentTurnThenVerifyAllActionsTriggered() {
        // given turn
        Turn turn = new Turn();
        turn.setTurnId(1);

        Mockito.when(turnRepository.findFirstByOrderByTurnIdDesc()).thenReturn(turn);

        // when processing current turn
        scheduler.processTurn();

        // then verify all actions triggered
        Mockito.verify(actionService).assignDefaultActions();
        Mockito.verify(actionService).processBuildingActions(1);
        Mockito.verify(actionService).processLocationActions(1);
        Mockito.verify(actionService).processQuestActions(1);
        Mockito.verify(actionService).processTradeActions(1);
        Mockito.verify(actionService).processDisaster(1);
        Mockito.verify(clanService).performClanTurnUpdates(1);
    }

    @Test
    public void testGivenTurnWhenMaxTurnsExceededThenVerifyNoActionsPerformed() {
        // given turn
        Turn turn = new Turn();
        turn.setTurnId(46);

        Mockito.when(turnRepository.findFirstByOrderByTurnIdDesc()).thenReturn(turn);

        // when processing current turn
        scheduler.processTurn();

        // then verify all actions triggered
        Mockito.verify(actionService, Mockito.never()).assignDefaultActions();
        Mockito.verify(actionService, Mockito.never()).processBuildingActions(1);
        Mockito.verify(actionService, Mockito.never()).processLocationActions(1);
        Mockito.verify(actionService, Mockito.never()).processQuestActions(1);
        Mockito.verify(actionService, Mockito.never()).processTradeActions(1);
        Mockito.verify(actionService, Mockito.never()).processDisaster(1);
        Mockito.verify(clanService, Mockito.never()).performClanTurnUpdates(1);
        Mockito.verify(turnRepository, Mockito.never()).save(Mockito.any());
    }

}
