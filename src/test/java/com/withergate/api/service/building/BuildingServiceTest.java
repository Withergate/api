package com.withergate.api.service.building;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BuildingServiceTest {

    private BuildingService buildingService;

    @Mock
    private CharacterService characterService;

    @Mock
    private ClanService clanService;

    @Mock
    private BuildingActionRepository buildingActionRepository;

    @Mock
    private BuildingDetailsRepository buildingDetailsRepository;

    @Mock
    private ClanNotificationRepository notificationRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        buildingService = new BuildingService(characterService, clanService, buildingActionRepository, buildingDetailsRepository,
                notificationRepository);
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
        details.setName("Monument");
        details.setIdentifier(BuildingDetails.BuildingName.MONUMENT);
        Mockito.when(buildingDetailsRepository.getOne(BuildingDetails.BuildingName.MONUMENT)).thenReturn(details);

        List<BuildingAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(buildingActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when constructing new building
        buildingService.processBuildingActions(1);

        // then verify progress saved
        ArgumentCaptor<Character> characterCaptor = ArgumentCaptor.forClass(Character.class);
        Mockito.verify(characterService).save(characterCaptor.capture());
        Assert.assertTrue(characterCaptor.getValue().getClan().getBuildings().containsKey(BuildingDetails.BuildingName.MONUMENT));
        Assert.assertEquals(4, characterCaptor.getValue().getClan().getBuildings().get(BuildingDetails.BuildingName.MONUMENT).getProgress());

        ArgumentCaptor<BuildingAction> actionCaptor = ArgumentCaptor.forClass(BuildingAction.class);
        Mockito.verify(buildingActionRepository).save(actionCaptor.capture());
        Assert.assertEquals(ActionState.COMPLETED, actionCaptor.getValue().getState());

        Mockito.verify(notificationRepository).save(Mockito.any());
    }
}
