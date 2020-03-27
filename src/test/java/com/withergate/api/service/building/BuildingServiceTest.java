package com.withergate.api.service.building;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.type.EndBonusType;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.BuildingAction;
import com.withergate.api.game.model.action.BuildingAction.Type;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.character.TraitDetails;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.request.BuildingRequest;
import com.withergate.api.game.repository.action.BuildingActionRepository;
import com.withergate.api.game.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
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

    @Mock
    private CharacterService characterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setBuildingFame(1);

        buildingService = new BuildingServiceImpl(itemService, buildingActionRepository,
                buildingDetailsRepository, notificationService, randomService, properties, characterService);
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBuildingVisitRequestWhenClanDoesNotHaveBuildingThenVerifyExceptionThrown() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding("Forge");
        request.setType(Type.VISIT);

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

        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("Forge");
        buildingDetails.setBonusType(BonusType.CRAFTING);
        Mockito.when(buildingService.getBuildingDetails("Forge")).thenReturn(buildingDetails);

        // when creating building action
        buildingService.saveBuildingAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenBuildingVisitRequestWhenBuildingInsufficientLevelThenVerifyExceptionThrown() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding("Forge");
        request.setType(Type.VISIT);

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

        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("Forge");
        Mockito.when(buildingService.getBuildingDetails("Forge")).thenReturn(buildingDetails);

        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(0);
        clan.getBuildings().add(building);

        // when creating building action
        buildingService.saveBuildingAction(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenBuildingVisitRequestWhenBuildingSufficientLevelThenActionCreated() throws InvalidActionException {
        // given building request
        BuildingRequest request = new BuildingRequest();
        request.setCharacterId(1);
        request.setBuilding("Forge");
        request.setType(Type.VISIT);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(10);
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("Forge");
        buildingDetails.setVisitJunkCost(10);
        buildingDetails.setVisitable(true);
        Mockito.when(buildingService.getBuildingDetails("Forge")).thenReturn(buildingDetails);

        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().add(building);

        // when creating building action
        buildingService.saveBuildingAction(request, 1);

        // then verify action created
        ArgumentCaptor<BuildingAction> captor = ArgumentCaptor.forClass(BuildingAction.class);
        Mockito.verify(buildingActionRepository).save(captor.capture());

        assertEquals(character, captor.getValue().getCharacter());
        assertEquals("Forge", captor.getValue().getBuilding());
    }

    @Test
    public void testGivenBuildingActionWhenConstructingNewBuildingThenVerifyProgressSavedAndActionHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier("MONUMENT");
        details.setEndBonusType(EndBonusType.FAME_INCOME);

        Building building = new Building();
        building.setLevel(0);
        building.setProgress(0);
        building.setDetails(details);
        clan.getBuildings().add(building);

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("MONUMENT");
        action.setType(BuildingAction.Type.CONSTRUCT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when constructing new building
        Mockito.when(buildingDetailsRepository.getOne("MONUMENT")).thenReturn(details);

        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(4, character.getClan().getBuilding("MONUMENT").getProgress());

        Assert.assertEquals(ActionState.COMPLETED, action.getState());

        Mockito.verify(notificationService).save(Mockito.any());
    }

    @Test
    public void testGivenBuildingActionWhenImprovingExistingBuildingThenVerifyLevelUpdated() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("FORGE");
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier("FORGE");
        Mockito.when(buildingDetailsRepository.getOne("FORGE")).thenReturn(details);

        Building building = new Building();
        building.setDetails(details);
        building.setLevel(0);
        building.setProgress(6);
        clan.getBuildings().add(building);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when improving existing building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(0, character.getClan().getBuilding("FORGE").getProgress());
        Assert.assertEquals(1, character.getClan().getBuilding("FORGE").getLevel());
    }

    @Test
    public void testGivenBuildingActionWhenImprovingExistingBuildingWithTraitThenVerifyProgressHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(4);
        character.setState(CharacterState.BUSY);
        character.setClan(clan);
        character.setName("Test");

        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setBonus(2);
        traitDetails.setIdentifier("BUILDER");
        traitDetails.setBonusType(BonusType.CONSTRUCT);
        Trait trait = new Trait();
        trait.setDetails(traitDetails);
        trait.setActive(true);
        character.getTraits().add(trait);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("QUARTERS");
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier("QUARTERS");
        Mockito.when(buildingDetailsRepository.getOne("QUARTERS")).thenReturn(details);

        Building building = new Building();
        building.setDetails(details);
        building.setLevel(0);
        building.setProgress(2);
        clan.getBuildings().add(building);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when improving existing building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(8, character.getClan().getBuilding("QUARTERS").getProgress());
    }

    @Test
    public void testGivenBuildingActionWhenImprovingExistingBuildingWithGearThenVerifyProgressHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);

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
        action.setBuilding("QUARTERS");
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier("QUARTERS");
        Mockito.when(buildingDetailsRepository.getOne("QUARTERS")).thenReturn(details);

        Building building = new Building();
        building.setDetails(details);
        building.setLevel(0);
        building.setProgress(2);
        clan.getBuildings().add(building);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when improving existing building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        Assert.assertEquals(9, character.getClan().getBuilding("QUARTERS").getProgress());
    }

    @Test
    public void testGivenNameWhenGettingBuildingDetailsThenVerifyCorrectDetailsReturned() {
        // given name
        String name = "FORGE";

        // when getting building details
        BuildingDetails details = new BuildingDetails();
        details.setIdentifier("FORGE");
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
        details.setIdentifier("FORGE");
        details.setVisitable(true);
        details.setBonusType(BonusType.CRAFTING);

        List<BuildingDetails> detailsList = new ArrayList<>();
        detailsList.add(details);

        Mockito.when(buildingDetailsRepository.findAll()).thenReturn(detailsList);

        List<BuildingDetails> result = buildingService.getAllBuildingDetails();

        // then verify correct list returned
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testGivenCraftingActionWhenForgingThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier("FORGE");
        details.setBonusType(BonusType.CRAFTING);
        details.setItemType(ItemType.WEAPON);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().add(building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("FORGE");
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(0),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.WEAPON));
    }

    @Test
    public void testGivenCraftingActionWhenCraftingOutfitThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier("RAGS_SHOP");
        details.setBonusType(BonusType.CRAFTING);
        details.setItemType(ItemType.OUTFIT);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().add(building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("RAGS_SHOP");
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(0),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.OUTFIT));
    }

    @Test
    public void testGivenCraftingActionWhenCraftingGearThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier("WORKSHOP");
        details.setBonusType(BonusType.CRAFTING);
        details.setItemType(ItemType.GEAR);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().add(building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("WORKSHOP");
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(0),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.GEAR));
    }

    @Test
    public void testGivenCraftingActionWhenCraftingGearWithHammerThenVerifyItemServiceCalled() {
        // given action
        Character character = new Character();
        character.setCraftsmanship(3);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        ItemDetails gearDetails = new ItemDetails();
        gearDetails.setBonus(1);
        gearDetails.setBonusType(BonusType.CRAFTING);
        gearDetails.setItemType(ItemType.GEAR);
        Item gear = new Item();
        gear.setDetails(gearDetails);
        character.getItems().add(gear);

        BuildingDetails details = new BuildingDetails();
        details.setIdentifier("WORKSHOP");
        details.setBonusType(BonusType.CRAFTING);
        details.setItemType(ItemType.GEAR);
        Building building = new Building();
        building.setLevel(1);
        building.setDetails(details);
        clan.getBuildings().add(building);

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding("WORKSHOP");
        action.setType(Type.VISIT);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when processing actions with forge
        buildingService.processBuildingActions(1);

        // then verify item service called
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.eq(1),
                Mockito.any(ClanNotification.class), Mockito.eq(ItemType.GEAR));
    }

    @Test
    public void testGivenClanWhenProcessingPassiveActionsThenVerifyBonusesAwarded() {
        // given clan with monument
        Clan clan = new Clan();
        clan.setFame(10);
        clan.setFood(5);
        clan.setInformation(0);
        clan.setCharacters(new HashSet<>());

        BuildingDetails monumentDetails = new BuildingDetails();
        monumentDetails.setIdentifier("MONUMENT");
        monumentDetails.setEndBonusType(EndBonusType.FAME_INCOME);
        monumentDetails.setEndBonus(1);
        Building monument = new Building();
        monument.setDetails(monumentDetails);
        monument.setLevel(2);
        clan.getBuildings().add(monument);

        BuildingDetails gmoDetails = new BuildingDetails();
        gmoDetails.setIdentifier("GMO_FARM");
        gmoDetails.setEndBonusType(EndBonusType.FOOD_INCOME);
        gmoDetails.setEndBonus(2);
        Building farm = new Building();
        farm.setDetails(gmoDetails);
        farm.setLevel(1);
        clan.getBuildings().add(farm);

        BuildingDetails studyDetails = new BuildingDetails();
        studyDetails.setIdentifier("STUDY");
        studyDetails.setEndBonusType(EndBonusType.INFORMATION_INCOME);
        studyDetails.setEndBonus(1);
        Building study = new Building();
        study.setDetails(studyDetails);
        study.setLevel(1);
        clan.getBuildings().add(study);

        // when processing passive acitons
        buildingService.processPassiveBuildingBonuses(1, clan);

        // then verify fame and food awarded
        Assert.assertEquals(12, clan.getFame());
        Assert.assertEquals(7, clan.getFood());
        Assert.assertEquals(1, clan.getInformation());
    }

}
