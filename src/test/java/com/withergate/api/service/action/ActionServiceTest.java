package com.withergate.api.service.action;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.BuildingAction.Type;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.action.LocationAction.LocationActionType;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.item.WeaponType;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.request.ArenaRequest;
import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.LocationService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.trade.TradeService;

import java.util.HashMap;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ActionServiceTest {

    private ActionServiceImpl actionService;

    @Mock
    private CharacterService characterService;

    @Mock
    private BuildingService buildingService;

    @Mock
    private LocationService locationService;

    @Mock
    private QuestService questService;

    @Mock
    private TradeService tradeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties gameProperties = new GameProperties();
        gameProperties.setNeighborhoodEncounterProbability(0);
        gameProperties.setNeighborhoodJunkBonus(0);
        gameProperties.setWastelandEncounterProbability(20);
        gameProperties.setWastelandJunkBonus(2);
        gameProperties.setCityEncounterProbability(40);
        gameProperties.setCityJunkBonus(4);

        actionService = new ActionServiceImpl(characterService, locationService, gameProperties, buildingService,
                questService, tradeService);
    }

    @Test
    public void testGivenLocationRequestWhenCreatingLocationActionThenVerifyEntitiesSaved() throws InvalidActionException {
        // given location request
        LocationRequest request = new LocationRequest();
        request.setCharacterId(1);
        request.setLocation(Location.WASTELAND);
        request.setType(LocationAction.LocationActionType.VISIT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.WASTELAND);
        description.setScouting(true);
        Mockito.when(locationService.getLocationDescription(Location.WASTELAND)).thenReturn(description);

        // when creating location action
        actionService.createLocationAction(request, 1);

        // then verify action saved and character updated
        ArgumentCaptor<LocationAction> captorAction = ArgumentCaptor.forClass(LocationAction.class);
        ArgumentCaptor<Character> captorCharacter = ArgumentCaptor.forClass(Character.class);

        Mockito.verify(locationService).saveLocationAction(captorAction.capture());
        Mockito.verify(characterService).save(captorCharacter.capture());

        assertEquals(ActionState.PENDING, captorAction.getValue().getState());
        assertEquals(character, captorAction.getValue().getCharacter());
        assertEquals(Location.WASTELAND, captorAction.getValue().getLocation());

        assertEquals(CharacterState.BUSY, captorCharacter.getValue().getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenLocationRequestWhenScoutingInNonSupportedLocationThenVerifyExceptionThrown() throws InvalidActionException {
        // given location request
        LocationRequest request = new LocationRequest();
        request.setCharacterId(1);
        request.setLocation(Location.NEIGHBORHOOD);
        request.setType(LocationActionType.SCOUT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.NEIGHBORHOOD);
        description.setScouting(false);
        Mockito.when(locationService.getLocationDescription(Location.NEIGHBORHOOD)).thenReturn(description);

        // when creating location action
        actionService.createLocationAction(request, 1);

        // then expect exception
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
        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating location action
        actionService.createArenaAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenArenaRequestWhenCharacterEquipsRangedWeaponThenVerifyExceptionThrown() throws InvalidActionException {
        // given arena request
        ArenaRequest request = new ArenaRequest();
        request.setCharacterId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setArena(false);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);

        WeaponDetails weaponDetails = new WeaponDetails();
        weaponDetails.setType(WeaponType.RANGED);
        Weapon weapon = new Weapon();
        weapon.setDetails(weaponDetails);
        character.setWeapon(weapon);

        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating location action
        actionService.createArenaAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBuildingRequestWhenCharacterNotReadyThenVerifyExceptionThrown() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding(BuildingName.FORGE);
        request.setType(Type.CONSTRUCT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.BUSY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating building action
        actionService.createBuildingAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBuildingVisitRequestWhenClanDoesNotHaveBuildingThenVerifyExceptionThrown() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding(BuildingName.FORGE);
        request.setType(Type.VISIT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setBuildings(new HashMap<>());
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier(BuildingName.FORGE);
        Mockito.when(buildingService.getBuildingDetails(BuildingName.FORGE)).thenReturn(buildingDetails);

        // when creating building action
        actionService.createBuildingAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBuildingVisitRequestWhenBuildingInsufficientLevelThenVerifyExceptionThrown() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding(BuildingName.FORGE);
        request.setType(Type.VISIT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setBuildings(new HashMap<>());
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier(BuildingName.FORGE);
        Mockito.when(buildingService.getBuildingDetails(BuildingName.FORGE)).thenReturn(buildingDetails);

        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(0);
        clan.getBuildings().put(BuildingName.FORGE, building);

        // when creating building action
        actionService.createBuildingAction(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenBuildingVisitRequestWhenBuildingSufficientLevelThenActionCreated() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding(BuildingName.FORGE);
        request.setType(Type.VISIT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(10);
        clan.setBuildings(new HashMap<>());
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier(BuildingName.FORGE);
        buildingDetails.setVisitJunkCost(10);
        buildingDetails.setVisitable(true);
        Mockito.when(buildingService.getBuildingDetails(BuildingName.FORGE)).thenReturn(buildingDetails);

        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().put(BuildingName.FORGE, building);

        // when creating building action
        actionService.createBuildingAction(request, 1);

        // then verify action created
        ArgumentCaptor<BuildingAction> captor = ArgumentCaptor.forClass(BuildingAction.class);
        Mockito.verify(buildingService).saveBuildingAction(captor.capture());

        assertEquals(character, captor.getValue().getCharacter());
        assertEquals(BuildingName.FORGE, captor.getValue().getBuilding());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenQuestRequestWhenNoQuestsForClanThenVerifyExceptionThrown() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating quest action
        actionService.createQuestAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenQuestRequestWhenQuestAlreadyCompletedThenVerifyExceptionThrown() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Quest quest = new Quest();
        quest.setId(1);
        quest.setCompleted(true);
        clan.getQuests().add(quest);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating quest action
        actionService.createQuestAction(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenQuestRequestWhenQuestAvailableThenVerifyActionSaved() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Quest quest = new Quest();
        quest.setId(1);
        quest.setCompleted(false);
        clan.getQuests().add(quest);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.load(1)).thenReturn(character);

        // when creating building action
        actionService.createQuestAction(request, 1);

        // then verify action saved
        ArgumentCaptor<QuestAction> captor = ArgumentCaptor.forClass(QuestAction.class);
        Mockito.verify(questService).saveQuestAction(captor.capture());
        assertEquals(character, captor.getValue().getCharacter());
        assertEquals(quest, captor.getValue().getQuest());
    }

    @Test
    public void testGivenTurnIdWhenProcessingLocationActionsThenVerifyAllProcessedTriggered() {
        // given turn ID
        int turnId = 1;

        // when processing location actions
        actionService.processLocationActions(turnId);

        // then verify all processed triggered
        Mockito.verify(locationService).processLocationActions(turnId);
        Mockito.verify(locationService).processArenaActions(turnId);
    }

    @Test
    public void testGivenTurnIdWhenProcessingBuildingActionsThenVerifyAllProcessedTriggered() {
        // given turn ID
        int turnId = 1;

        // when processing location actions
        actionService.processBuildingActions(turnId);

        // then verify all processed triggered
        Mockito.verify(buildingService).processBuildingActions(turnId);
        Mockito.verify(buildingService).processPassiveBuildingBonuses(turnId);
    }

}
