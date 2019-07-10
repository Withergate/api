package com.withergate.api.service.location;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.action.LocationAction.LocationActionType;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.GearDetails;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.repository.LocationDescriptionRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        locationService = new LocationServiceImpl(locationActionRepository, randomService,
                encounterService, itemService, notificationService, locationDescriptionRepository);
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
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setScavenge(5);
        character.setState(CharacterState.BUSY);

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
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
        character.setScavenge(3);
        character.setName("Misty Fox");

        GearDetails details = new GearDetails();
        details.setBonusType(BonusType.SCAVENGE_FOOD);
        details.setBonus(2);
        Gear gear = new Gear();
        gear.setDetails(details);
        character.setGear(gear);

        Trait trait = new Trait();
        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setIdentifier(TraitName.HUNTER);
        traitDetails.setBonus(2);
        trait.setDetails(traitDetails);
        character.getTraits().put(TraitName.HUNTER, trait);

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
        character.setScavenge(3);
        character.setName("Loud Garry");

        GearDetails details = new GearDetails();
        details.setBonusType(BonusType.SCAVENGE_JUNK);
        details.setBonus(2);
        Gear gear = new Gear();
        gear.setDetails(details);
        character.setGear(gear);

        Trait trait = new Trait();
        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setIdentifier(TraitName.HOARDER);
        traitDetails.setBonus(2);
        trait.setDetails(traitDetails);
        character.getTraits().put(TraitName.HOARDER, trait);

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

    @Test
    public void testGivenLocationWhenGettingDescriptionThenVerifyDescriptionReturned() {
        // given location
        Location location = Location.CITY_CENTER;

        LocationDescription description = new LocationDescription();
        description.setLocation(Location.CITY_CENTER);
        Mockito.when(locationDescriptionRepository.getOne(Location.CITY_CENTER)).thenReturn(description);

        // when getting description
        LocationDescription result = locationService.getLocationDescription(location);

        // then verify correct description returned
        Assert.assertEquals(description, result);
    }

    @Test
    public void testGivenLocationActionWhenSavingThenVerifyRepositoryCalled() {
        // given action
        LocationAction action = new LocationAction();
        action.setLocation(Location.NEIGHBORHOOD);

        // when saving
        locationService.saveLocationAction(action);

        // then verify repository called
        Mockito.verify(locationActionRepository).save(action);
    }

}
