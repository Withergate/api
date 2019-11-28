package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ArenaAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.request.ArenaRequest;
import com.withergate.api.repository.action.ArenaActionRepository;
import com.withergate.api.repository.arena.ArenaStatsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.exception.InvalidActionException;
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

    @Mock
    private CharacterService characterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        arenaService = new ArenaServiceImpl(arenaActionRepository, combatService, notificationService, arenaStatsRepository,
                characterService);
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
        Assert.assertTrue(captor.getValue().contains(character1));
        Assert.assertTrue(captor.getValue().contains(character2));

        // check actions completed
        Assert.assertEquals(ActionState.COMPLETED, action1.getState());
        Assert.assertEquals(ActionState.COMPLETED, action2.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenArenaRequestWhenCharacterAlreadyInArenaForClanThenVerifyExceptionThrown() throws InvalidActionException {
        // given arena request
        ArenaRequest request = new ArenaRequest();
        request.setCharacterId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setArena(true);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        // when creating location action
        arenaService.saveArenaAction(request, 1);

        // then expect exception
    }

    private Character mockCharacter(int id, String name) {
        Character character = new Character();
        character.setId(id);
        character.setName(name);

        return character;
    }

}
