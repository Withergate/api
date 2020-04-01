package com.withergate.api.service.building;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.BuildingAction;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.character.TraitDetails;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.repository.action.BuildingActionRepository;
import com.withergate.api.game.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
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

    @Test
    public void testGivenBuildingActionWhenConstructingNewBuildingThenVerifyProgressSavedAndActionHandled() {
        // given building action
        Clan clan = new Clan();
        clan.setId(1);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier("MONUMENT");
        details.setPassiveBonusType(PassiveBonusType.FAME_INCOME);

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
        details.setBonusType(BonusType.CRAFTING);

        List<BuildingDetails> detailsList = new ArrayList<>();
        detailsList.add(details);

        Mockito.when(buildingDetailsRepository.findAll()).thenReturn(detailsList);

        List<BuildingDetails> result = buildingService.getAllBuildingDetails();

        // then verify correct list returned
        Assert.assertEquals(1, result.size());
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
        monumentDetails.setPassiveBonusType(PassiveBonusType.FAME_INCOME);
        monumentDetails.setPassiveBonus(1);
        Building monument = new Building();
        monument.setDetails(monumentDetails);
        monument.setLevel(2);
        clan.getBuildings().add(monument);

        BuildingDetails gmoDetails = new BuildingDetails();
        gmoDetails.setIdentifier("GMO_FARM");
        gmoDetails.setPassiveBonusType(PassiveBonusType.FOOD_INCOME);
        gmoDetails.setPassiveBonus(2);
        Building farm = new Building();
        farm.setDetails(gmoDetails);
        farm.setLevel(1);
        clan.getBuildings().add(farm);

        BuildingDetails studyDetails = new BuildingDetails();
        studyDetails.setIdentifier("STUDY");
        studyDetails.setPassiveBonusType(PassiveBonusType.INFORMATION_INCOME);
        studyDetails.setPassiveBonus(1);
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
