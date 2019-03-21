package com.withergate.api.service.action;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.LocationService;

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
    private ClanService clanService;

    @Mock
    private BuildingService buildingService;

    @Mock
    private LocationService locationService;

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

        actionService = new ActionService(characterService, locationService, clanService, gameProperties, buildingService);
    }

    @Test
    public void testGivenLocationRequestWhenCreatingLocationActionThenVerifyEntitiesSaved() throws InvalidActionException {
        // given location request
        LocationRequest request = new LocationRequest();
        request.setCharacterId(1);
        request.setLocation(Location.WASTELAND);

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

}
