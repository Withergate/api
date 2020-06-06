package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.RestingAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.request.RestingRequest;
import com.withergate.api.game.repository.action.RestingActionRepository;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class RestingServiceTest {

    private RestingService restingService;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private RestingActionRepository repository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setHealing(2);

        restingService = new RestingServiceImpl(characterRepository, repository, notificationService, randomService,
                properties);
    }

    @Test
    public void testGivenReadyCharacterWhenCreatingRestingActionThenVerifyActionCreated() throws Exception {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when creating resting action
        RestingRequest restingRequest = new RestingRequest();
        restingRequest.setCharacterId(1);
        restingService.saveAction(restingRequest, 1);

        // then verify action created
        Mockito.verify(repository).save(Mockito.any(RestingAction.class));
        Assert.assertEquals(CharacterState.BUSY, character.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBusyCharacterWhenCreatingRestingActionThenExpectException() throws Exception {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        character.setState(CharacterState.BUSY);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when creating resting action
        RestingRequest restingRequest = new RestingRequest();
        restingRequest.setCharacterId(1);
        restingService.saveAction(restingRequest, 1);

        // then expect exception
    }

    @Test
    public void testGivenInjuredCharacterWhenRestingThenVerifyCharacterHealed() {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setState(CharacterState.BUSY);
        character.setHitpoints(5);
        character.setMaxHitpoints(10);
        character.setClan(clan);

        RestingAction action = new RestingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        Mockito.when(repository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when performing resting
        restingService.runActions(1);

        // then verify character healed
        Assert.assertEquals(7, character.getHitpoints());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

}
