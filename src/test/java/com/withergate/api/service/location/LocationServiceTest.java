package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.game.model.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.LocationAction;
import com.withergate.api.game.model.action.LocationAction.LocationActionType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.character.TraitDetails;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.location.LocationDescription;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.game.repository.LocationDescriptionRepository;
import com.withergate.api.game.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class LocationServiceTest {

    private LocationServiceImpl locationService;

    @Mock
    private LocationActionRepository locationActionRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private EncounterService encounterService;

    @Mock
    private ItemService itemService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private LocationDescriptionRepository locationDescriptionRepository;

    @Mock
    private CharacterService characterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        locationService = new LocationServiceImpl(locationActionRepository, randomService,
                encounterService, itemService, notificationService, locationDescriptionRepository, characterService);
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
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.WASTELAND);
        description.setScouting(true);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        // when creating location action
        locationService.saveLocationAction(request, 1);

        // then verify action saved and character updated
        ArgumentCaptor<LocationAction> captorAction = ArgumentCaptor.forClass(LocationAction.class);

        Mockito.verify(locationActionRepository).save(captorAction.capture());

        assertEquals(ActionState.PENDING, captorAction.getValue().getState());
        assertEquals(character, captorAction.getValue().getCharacter());
        assertEquals(Location.WASTELAND, captorAction.getValue().getLocation());

        assertEquals(CharacterState.BUSY, character.getState());
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
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.NEIGHBORHOOD);
        description.setScouting(false);
        Mockito.when(locationDescriptionRepository.getOne(Location.NEIGHBORHOOD)).thenReturn(description);

        // when creating location action
        locationService.saveLocationAction(request, 1);

        // then expect exception
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
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        LocationDescription description = new LocationDescription();
        description.setEncounterChance(25);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(10); // low roll

        locationService.processLocationActions(1);

        // then verify encounter triggered
        Mockito.verify(encounterService).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND));
    }

    @Test
    public void testGivenPendingLocationActionWhenHandlingSuccessfulEncounterThenVerifyPartialResourcesAwarded() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");
        clan.setFood(10);
        clan.setJunk(12);

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(4);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        LocationDescription description = new LocationDescription();
        description.setEncounterChance(25);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);
        Mockito.when(encounterService.handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND)))
                .thenReturn(true); // encounter success

        // when performing pending actions
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(10); // low roll

        locationService.processLocationActions(1);

        // then verify some resources awarded
        Assert.assertEquals(12, clan.getFood());
        Assert.assertEquals(14, clan.getJunk());
    }

    @Test
    public void testGivenPendingLocationActionWhenHandlingFailedEncounterThenVerifyNoResourcesAwarded() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");
        clan.setFood(10);
        clan.setJunk(12);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(4);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        LocationDescription description = new LocationDescription();
        description.setEncounterChance(25);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);
        Mockito.when(encounterService.handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND)))
                .thenReturn(false); // encounter failure

        // when performing pending actions
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(10); // low roll

        locationService.processLocationActions(1);

        // then verify no resources awarded
        Assert.assertEquals(10, clan.getFood());
        Assert.assertEquals(12, clan.getJunk());
    }

    @Test
    public void testGivenPendingLocationActionWhenHandlingSuccessfulEncounterThenVerifyPartialInformationAwarded() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");
        clan.setInformation(5);

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setIntellect(4);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setType(LocationActionType.SCOUT);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        LocationDescription description = new LocationDescription();
        description.setEncounterChance(25);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);
        Mockito.when(encounterService.handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND)))
                .thenReturn(true); // encounter success

        // when performing pending actions
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(10); // low roll

        locationService.processLocationActions(1);

        // then verify partial information awarded
        Assert.assertEquals(7, clan.getInformation());
    }

    @Test
    public void testGivenPendingLocationActionWhenLowLootDiceRollThenVerifyLootFound() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(5);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setLocation(Location.WASTELAND);

        LocationDescription description = new LocationDescription();
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions

        // high encounter roll followed by low loot roll
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(50, 5);

        locationService.processLocationActions(1);

        // then verify item generated
        Mockito.verify(itemService).generateItemForCharacter(Mockito.eq(character), Mockito.any(ClanNotification.class));
        Mockito.verify(encounterService, Mockito.never()).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.WASTELAND));
    }

    @Test
    public void testGivenPendingLocationActionWhenHighDiceRollsThenVerifyJunkAndFoodFound() {
        // given pending location action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(10);
        clan.setFood(5);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(5);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setLocation(Location.NEIGHBORHOOD);

        LocationDescription description = new LocationDescription();
        Mockito.when(locationDescriptionRepository.getOne(Location.NEIGHBORHOOD)).thenReturn(description);

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions

        // high encounter roll followed by high loot roll
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(50);

        locationService.processLocationActions(1);

        // then verify clan saved with updated junk
        Mockito.verify(encounterService, Mockito.never()).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.NEIGHBORHOOD));
        Mockito.verify(itemService, Mockito.never()).generateItemForCharacter(Mockito.eq(character), Mockito.any(ClanNotification.class));

        Assert.assertEquals(15, clan.getJunk());
        Assert.assertEquals(10, clan.getFood());
    }

    @Test
    public void testGivenCharacterWithBonusGearWhenExploringThenVerifyFoodBonusGranted() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setScavenge(3);
        character.setName("Misty Fox");

        ItemDetails details = new ItemDetails();
        details.setBonusType(BonusType.SCAVENGE_FOOD);
        details.setBonus(2);
        details.setItemType(ItemType.GEAR);
        Item gear = new Item();
        gear.setDetails(details);
        character.getItems().add(gear);

        Trait trait = new Trait();
        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setIdentifier("HUNTER");
        traitDetails.setBonusType(BonusType.SCAVENGE_FOOD);
        traitDetails.setBonus(2);
        trait.setDetails(traitDetails);
        trait.setActive(true);
        character.getTraits().add(trait);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(0);
        clan.setJunk(0);
        character.setClan(clan);

        LocationAction action = new LocationAction();
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        action.setLocation(Location.WASTELAND);
        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.WASTELAND);
        description.setEncounterChance(0);
        description.setFoodBonus(2);
        description.setJunkBonus(2);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(50); // encounter roll

        // when exploring
        locationService.processLocationActions(1);

        // then verify bonuses granted
        Assert.assertEquals(9, clan.getFood());
        Assert.assertEquals(5, clan.getJunk());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenCharacterWithBonusGearWhenExploringThenVerifyJunkBonusGranted() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setScavenge(3);
        character.setName("Loud Garry");

        ItemDetails details = new ItemDetails();
        details.setBonusType(BonusType.SCAVENGE_JUNK);
        details.setBonus(2);
        details.setItemType(ItemType.GEAR);
        Item gear = new Item();
        gear.setDetails(details);
        character.getItems().add(gear);

        Trait trait = new Trait();
        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setIdentifier("HOARDER");
        traitDetails.setBonusType(BonusType.SCAVENGE_JUNK);
        traitDetails.setBonus(2);
        trait.setDetails(traitDetails);
        trait.setActive(true);
        character.getTraits().add(trait);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(0);
        clan.setJunk(0);
        character.setClan(clan);

        LocationAction action = new LocationAction();
        action.setType(LocationActionType.VISIT);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        action.setLocation(Location.WASTELAND);
        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.WASTELAND);
        description.setEncounterChance(0);
        description.setFoodBonus(2);
        description.setJunkBonus(2);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(50); // encounter roll

        // when exploring
        locationService.processLocationActions(1);

        // then verify bonuses granted
        Assert.assertEquals(5, clan.getFood());
        Assert.assertEquals(9, clan.getJunk());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenCharacterWhenScoutingThenVerifyClanInformationIncreased() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setHitpoints(10);
        character.setIntellect(4);
        character.setName("Loud Garry");

        Clan clan = new Clan();
        clan.setId(1);
        clan.setInformation(3);
        character.setClan(clan);

        LocationAction action = new LocationAction();
        action.setType(LocationActionType.SCOUT);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        action.setLocation(Location.WASTELAND);
        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.WASTELAND);
        description.setEncounterChance(0);
        description.setInformationBonus(1);
        Mockito.when(locationDescriptionRepository.getOne(Location.WASTELAND)).thenReturn(description);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(50); // encounter roll

        // when exploring
        locationService.processLocationActions(1);

        // then verify information increased
        Assert.assertEquals(8, clan.getInformation());
    }

}
