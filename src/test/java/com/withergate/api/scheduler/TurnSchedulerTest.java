package com.withergate.api.scheduler;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.scheduling.TurnScheduler;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.action.ActionService;
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
    private ActionService locationActionService;

    @Mock
    private CharacterService characterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        scheduler = new TurnScheduler(turnRepository, locationActionService, characterService);
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
        Mockito.verify(characterService).performCharacterHealing(1);
        Mockito.verify(locationActionService).processLocationActions(1);
    }
}
