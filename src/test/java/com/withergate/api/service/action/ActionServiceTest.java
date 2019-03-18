package com.withergate.api.service.action;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.IClanService;
import com.withergate.api.service.encounter.ICombatService;
import com.withergate.api.service.encounter.IEncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.IItemService;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

    @Mock
    private IClanService clanService;

    @Mock
    private ICombatService combatService;

    @Mock
    private BuildingActionRepository buildingActionRepository;

    @Mock
    private BuildingService buildingService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties gameProperties = new GameProperties();
        gameProperties.setNeighborhoodEncounterProbability(0);
        gameProperties.setNeighborhoodJunkMultiplier(1);
        gameProperties.setWastelandEncounterProbability(20);
        gameProperties.setWastelandJunkMultiplier(2);
        gameProperties.setCityEncounterProbability(40);
        gameProperties.setCityJunkMultiplier(2);

        actionService = new ActionService(characterService, locationActionRepository, buildingActionRepository,
                clanNotificationRepository,
                randomService, encounterService, itemService, clanService, combatService, gameProperties,
                buildingService);
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
    public void testGivenPendingLocationActionWhenLowEncounterDiceRollThenVerifyEncounterTriggered() {
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

    @Test
    public void testGivenPendingLocationActionWhenLowLootDiceRollThenVerifyLootFound() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(5);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions

        // high encounter roll followed by low loot roll
        Mockito.when(randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE)).thenReturn(50, 10);

        actionService.performPendingLocationActions(1);

        // then verify item generated
        Mockito.verify(itemService).generateItemForCharacter(Mockito.eq(character), Mockito.any(ClanNotification.class));
        Mockito.verify(encounterService, Mockito.never()).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND));
    }

    @Test
    public void testGivenPendingLocationActionWhenHighDiceRollsThenVerifyJunkFound() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(10);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(5);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(Location.NEIGHBORHOOD);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions

        // high encounter roll followed by high loot roll
        Mockito.when(randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE)).thenReturn(50, 60);

        actionService.performPendingLocationActions(1);

        // then verify clan saved with updated junk
        Mockito.verify(encounterService, Mockito.never()).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.NEIGHBORHOOD));
        Mockito.verify(itemService, Mockito.never()).generateItemForCharacter(Mockito.eq(character), Mockito.any(ClanNotification.class));

        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanService).saveClan(captor.capture());

        assertEquals(15, captor.getValue().getJunk());
    }
}
