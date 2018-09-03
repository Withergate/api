package com.withergate.api.service.action;

import com.withergate.api.model.Clan;
import com.withergate.api.model.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.IItemService;
import com.withergate.api.service.encounter.IEncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActionServiceTest {

    private ActionService actionService;

    @Mock
    private CharacterService characterService;

    @Mock
    private LocationActionRepository locationActionRepository;

    @Mock
    private ClanNotificationRepository clanNotificationRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private IEncounterService encounterService;

    @Mock
    private IItemService itemService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        actionService = new ActionService(characterService, locationActionRepository, clanNotificationRepository,
                randomService, encounterService, itemService);
    }

    @Test
    public void testGivenLocationRequestWhenCreatingLocationActionThenVerifyEntitiesSaved() throws InvalidActionException {
        // given location request
        LocationRequest request = new LocationRequest();
        request.setCharacterId(1);
        request.setLocation(Location.WASTELAND);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating location action
        actionService.createLocationAction(request, 1);

        // then verify action saved and character updated
        ArgumentCaptor<LocationAction> captorAction = ArgumentCaptor.forClass(LocationAction.class);
        ArgumentCaptor<Character> captorCharacter = ArgumentCaptor.forClass(Character.class);

        Mockito.verify(locationActionRepository).save(captorAction.capture());
        Mockito.verify(characterService).save(captorCharacter.capture());

        assertEquals(ActionState.PENDING, captorAction.getValue().getState());
        assertEquals(character, captorAction.getValue().getCharacter());
        assertEquals(Location.WASTELAND, captorAction.getValue().getLocation());

        assertEquals(CharacterState.BUSY, captorCharacter.getValue().getState());
    }

    @Test
    public void testGivenPendingLocationActionWhenLowDiceRollThenVerifyEncounterTriggered() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions
        Mockito.when(randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE)).thenReturn(10); // low roll

        actionService.performPendingLocationActions(1);

        // then verify encounter triggered
        Mockito.verify(encounterService).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND));
    }
}
