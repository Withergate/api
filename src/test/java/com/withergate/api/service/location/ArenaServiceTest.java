package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ArenaAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.repository.action.ArenaActionRepository;
import com.withergate.api.repository.arena.ArenaStatsRepository;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ArenaServiceTest {

    private ArenaServiceImpl arenaService;

    @Mock
    private ArenaActionRepository arenaActionRepository;

    @Mock
    private CombatService combatService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ArenaStatsRepository arenaStatsRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        arenaService = new ArenaServiceImpl(arenaActionRepository, combatService, notificationService, arenaStatsRepository);
    }

    @Test
    public void testGivenArenaActionWhenSavingActionThenVerifyActionSaved() {
        // given action
        ArenaAction action = new ArenaAction();
        action.setCharacter(new Character());
        action.setState(ActionState.PENDING);

        // when saving action
        arenaService.saveArenaAction(action);

        // then verify action saved
        Mockito.verify(arenaActionRepository).save(action);
    }

    @Test
    public void testGivenArenaActionsWhenProcessingArenaThenVerifyCombatInitiated() {
        // given actions
        List<ArenaAction> actions = new ArrayList<>();

        ArenaAction action1 = new ArenaAction();
        action1.setState(ActionState.PENDING);
        Character character1 = mockCharacter(1, "Quick Fox");
        action1.setCharacter(character1);
        actions.add(action1);

        ArenaAction action2 = new ArenaAction();
        action2.setState(ActionState.PENDING);
        Character character2 = mockCharacter(2, "Big Tom");
        action2.setCharacter(character2);
        actions.add(action2);

        Mockito.when(arenaActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions
        arenaService.processArenaActions(1);

        // then verify combat initiated
        ArgumentCaptor<List<Character>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(combatService).handleArenaFights(captor.capture());
        Assert.assertEquals(character1, captor.getValue().get(0));
        Assert.assertEquals(character2, captor.getValue().get(1));

        // check actions completed
        Assert.assertEquals(ActionState.COMPLETED, action1.getState());
        Assert.assertEquals(ActionState.COMPLETED, action2.getState());
    }

    private Character mockCharacter(int id, String name) {
        Character character = new Character();
        character.setId(id);
        character.setName(name);

        return character;
    }

}
