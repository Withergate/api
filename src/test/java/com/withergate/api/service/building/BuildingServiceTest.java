package com.withergate.api.service.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.BuildingAction.Type;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BuildingServiceTest {

    private BuildingServiceImpl buildingService;

    @Mock
    private BuildingActionRepository buildingActionRepository;

    @Mock
    private BuildingDetailsRepository buildingDetailsRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ItemService itemService;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setBuildingFame(1);

        buildingService = new BuildingServiceImpl(itemService, buildingActionRepository,
                buildingDetailsRepository, notificationService, randomService, properties);
    }

    @Test
    public void testGivenBuildingActionWhenConstructingNewBuildingThenVerifyProgressSavedAndActionHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier(BuildingDetails.BuildingName.MONUMENT);

        Building building = new Building();
        building.setLevel(0);
        building.setProgress(0);
        building.setDetails(details);
        clan.getBuildings().put(details.getIdentifier(), building);

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingDetails.BuildingName.MONUMENT);
        action.setType(BuildingAction.Type.CONSTRUCT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when constructing new building
        Mockito.when(buildingDetailsRepository.getOne(BuildingName.MONUMENT)).thenReturn(details);

        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertTrue(character.getClan().getBuildings().containsKey(BuildingDetails.BuildingName.MONUMENT));
        Assert.assertEquals(4, character.getClan().getBuildings().get(BuildingDetails.BuildingName.MONUMENT).getProgress());

        Assert.assertEquals(ActionState.COMPLETED, action.getState());

        Mockito.verify(notificationService).save(Mockito.any());
    }

    @Test
    public void testGivenBuildingActionWhenImprovingExistingBuildingThenVerifyLevelUpdated() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.FORGE);
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier(BuildingName.FORGE);
        Mockito.when(buildingDetailsRepository.getOne(BuildingName.FORGE)).thenReturn(details);

        Building building = new Building();
        building.setDetails(details);
        building.setLevel(0);
        building.setProgress(6);
        clan.getBuildings().put(BuildingName.FORGE, building);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when improving existing building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(0, character.getClan().getBuildings().get(BuildingName.FORGE).getProgress());
        Assert.assertEquals(1, character.getClan().getBuildings().get(BuildingName.FORGE).getLevel());
    }

    @Test
    public void testGivenBuildingActionWhenImprovingExistingBuildingWithTraitThenVerifyProgressHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setBonus(2);
        traitDetails.setIdentifier(TraitName.BUILDER);
        Trait trait = new Trait();
        trait.setDetails(traitDetails);
        trait.setActive(true);
        character.getTraits().put(TraitName.BUILDER, trait);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.QUARTERS);
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier(BuildingName.QUARTERS);
        Mockito.when(buildingDetailsRepository.getOne(BuildingName.QUARTERS)).thenReturn(details);

        Building building = new Building();
        building.setDetails(details);
        building.setLevel(0);
        building.setProgress(2);
        clan.getBuildings().put(BuildingName.QUARTERS, building);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when improving existing building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(8, character.getClan().getBuildings().get(BuildingName.QUARTERS).getProgress());
    }

    @Test
    public void testGivenBuildingActionWhenImprovingExistingBuildingWithGearThenVerifyProgressHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        ItemDetails gearDetails = new ItemDetails();
        gearDetails.setBonus(3);
        gearDetails.setBonusType(BonusType.CONSTRUCT);
        gearDetails.setItemType(ItemType.GEAR);
        Item gear = new Item();
        gear.setDetails(gearDetails);
        character.getItems().add(gear);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.QUARTERS);
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier(BuildingName.QUARTERS);
        Mockito.when(buildingDetailsRepository.getOne(BuildingName.QUARTERS)).thenReturn(details);

        Building building = new Building();
        building.setDetails(details);
        building.setLevel(0);
        building.setProgress(2);
        clan.getBuildings().put(BuildingName.QUARTERS, building);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when improving existing building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(9, character.getClan().getBuildings().get(BuildingName.QUARTERS).getProgress());
    }

    @Test
    public void testGivenNameWhenGettingBuildingDetailsThenVerifyCorrectDetailsReturned() {
        // given name
        BuildingName name = BuildingName.FORGE;

        // when getting building details
        BuildingDetails details = new BuildingDetails();
        details.setIdentifier(BuildingName.FORGE);
        details.setVisitable(true);
        Mockito.when(buildingDetailsRepository.getOne(name)).thenReturn(details);

        BuildingDetails result = buildingService.getBuildingDetails(name);

        // then verify correct details returned
        Assert.assertEquals(details, result);
    }

    @Test
    public void testGiveServiceWhenGettingAllBuildingDetailsThenVerifyCorrectListReturned() {
        // given service when getting building details
        BuildingDetails details = new BuildingDetails();
        details.setIdentifier(BuildingName.FORGE);
        details.setVisitable(true);

        List<BuildingDetails> detailsList = new ArrayList<>();
        detailsList.add(details);

        Mockito.when(buildingDetailsRepository.findAll()).thenReturn(detailsList);

        List<BuildingDetails> result = buildingService.getAllBuildingDetails();

        // then verify correct list returned
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testGivenBuildingActionWhenSavingActionThenVerifyActionSaved() {
        // given building action
        BuildingAction action = new BuildingAction();
        action.setBuilding(BuildingName.RAGS_SHOP);
        action.setCharacter(new Character());
        action.setState(ActionState.PENDING);

        // when saving action
        buildingService.saveBuildingAction(action);

        // then verify action saved
        Mockito.verify(buildingActionRepository).save(action);
    }

    @Test
    public void testGivenCraftingActionWhenForgingThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());
        character.setClan(clan);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier(BuildingName.FORGE);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().put(BuildingName.FORGE, building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.FORGE);
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(1), Mockito.eq(0),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.WEAPON));
    }

    @Test
    public void testGivenCraftingActionWhenCraftingOutfitThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());
        character.setClan(clan);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier(BuildingName.RAGS_SHOP);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().put(BuildingName.RAGS_SHOP, building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.RAGS_SHOP);
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(1), Mockito.eq(0),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.OUTFIT));
    }

    @Test
    public void testGivenCraftingActionWhenCraftingGearThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());
        character.setClan(clan);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier(BuildingName.WORKSHOP);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().put(BuildingName.WORKSHOP, building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.WORKSHOP);
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(1), Mockito.eq(0),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.GEAR));
    }

    @Test
    public void testGivenCraftingActionWhenCraftingGearWithHammerThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setBuildings(new HashMap<>());
        character.setClan(clan);

        ItemDetails gearDetails = new ItemDetails();
        gearDetails.setBonus(1);
        gearDetails.setBonusType(BonusType.CRAFTING);
        gearDetails.setItemType(ItemType.GEAR);
        Item gear = new Item();
        gear.setDetails(gearDetails);
        character.getItems().add(gear);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier(BuildingName.WORKSHOP);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().put(BuildingName.WORKSHOP, building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(BuildingName.WORKSHOP);
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(1), Mockito.eq(1),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.GEAR));
    }

    @Test
    public void testGivenClanWhenProcessingPassiveActionsThenVerifyBonusesAwarded() {
        // given clan with monument
        Clan clan = new Clan();
        clan.setBuildings(new HashMap<>());
        clan.setFame(10);
        clan.setFood(5);
        clan.setInformation(0);
        clan.setCharacters(new HashSet<>());

        BuildingDetails monumentDetails = new BuildingDetails();
        monumentDetails.setIdentifier(BuildingName.MONUMENT);
        Building monument = new Building();
        monument.setDetails(monumentDetails);
        monument.setLevel(2);
        clan.getBuildings().put(BuildingName.MONUMENT, monument);

        BuildingDetails gmoDetails = new BuildingDetails();
        gmoDetails.setIdentifier(BuildingName.GMO_FARM);
        Building farm = new Building();
        farm.setDetails(monumentDetails);
        farm.setLevel(1);
        clan.getBuildings().put(BuildingName.GMO_FARM, farm);

        BuildingDetails groundsDetails = new BuildingDetails();
        groundsDetails.setIdentifier(BuildingName.TRAINING_GROUNDS);
        Building grounds = new Building();
        grounds.setDetails(groundsDetails);
        grounds.setLevel(0);
        clan.getBuildings().put(BuildingName.TRAINING_GROUNDS, grounds);

        BuildingDetails studyDetails = new BuildingDetails();
        studyDetails.setIdentifier(BuildingName.STUDY);
        Building study = new Building();
        study.setDetails(studyDetails);
        study.setLevel(1);
        clan.getBuildings().put(BuildingName.STUDY, study);

        // when processing passive acitons
        buildingService.processPassiveBuildingBonuses(1, clan);

        // then verify fame and food awarded
        Assert.assertEquals(12, clan.getFame());
        Assert.assertEquals(7, clan.getFood());
        Assert.assertEquals(1, clan.getInformation());
    }

}
