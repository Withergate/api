package com.withergate.api.service.location;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.encounter.ICombatService;
import com.withergate.api.service.item.ItemService;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class LocationServiceTest {

    private LocationService locationService;

    @Mock
    private LocationActionRepository locationActionRepository;

    private GameProperties gameProperties;

    @Mock
    private ClanService clanService;

    @Mock
    private CharacterService characterService;

    @Mock
    private ClanNotificationRepository clanNotificationRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private EncounterService encounterService;

    @Mock
    private ItemService itemService;

    @Mock
    private ICombatService combatService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        gameProperties = new GameProperties();
        gameProperties.setNeighborhoodJunkMultiplier(1);
        gameProperties.setRareItemChance(10);
        gameProperties.setWastelandEncounterProbability(20);
        gameProperties.setWastelandJunkMultiplier(2);
        gameProperties.setCityEncounterProbability(20);
        gameProperties.setCityJunkMultiplier(2);

        locationService = new LocationService(locationActionRepository, gameProperties, clanService, characterService,
                clanNotificationRepository, randomService, encounterService, itemService, combatService);
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
        Mockito.when(randomService.getRandomInt(1, RandomService.K100)).thenReturn(10); // low roll

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

        List<LocationAction> actions = new ArrayList<>();
        actions.add(action);

        Mockito.when(locationActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when performing pending actions

        // high encounter roll followed by low loot roll
        Mockito.when(randomService.getRandomInt(1, RandomService.K100)).thenReturn(50, 10);

        locationService.processLocationActions(1);

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
        Mockito.when(randomService.getRandomInt(1, RandomService.K100)).thenReturn(50, 60);

        locationService.processLocationActions(1);

        // then verify clan saved with updated junk
        Mockito.verify(encounterService, Mockito.never()).handleEncounter(Mockito.any(), Mockito.eq(character), Mockito.eq(Location.NEIGHBORHOOD));
        Mockito.verify(itemService, Mockito.never()).generateItemForCharacter(Mockito.eq(character), Mockito.any(ClanNotification.class));

        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanService).saveClan(captor.capture());

        assertEquals(15, captor.getValue().getJunk());
    }
}
