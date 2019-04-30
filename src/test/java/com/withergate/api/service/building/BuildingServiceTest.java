package com.withergate.api.service.building;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BuildingServiceTest {

    private BuildingServiceImpl buildingService;

    @Mock
    private CharacterService characterService;

    @Mock
    private ClanService clanService;

    @Mock
    private BuildingActionRepository buildingActionRepository;

    @Mock
    private BuildingDetailsRepository buildingDetailsRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ItemService itemService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        buildingService = new BuildingServiceImpl(characterService, clanService, itemService, buildingActionRepository, buildingDetailsRepository,
                notificationService);
    }

    @Test
    public void testGivenBuildingActionWhenConstructingNewBuildingThenVerifyProgressSavedAndActionHandled() {
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
        action.setBuilding(BuildingDetails.BuildingName.MONUMENT);
        action.setType(BuildingAction.Type.CONSTRUCT);

        BuildingDetails details = new BuildingDetails();
        details.setCost(10);
        details.setIdentifier(BuildingDetails.BuildingName.MONUMENT);
        Mockito.when(buildingDetailsRepository.getOne(BuildingDetails.BuildingName.MONUMENT)).thenReturn(details);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when constructing new building
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

}
