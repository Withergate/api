package com.withergate.api.service.disaster;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.DisasterAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.disaster.DisasterDetails;
import com.withergate.api.game.model.disaster.DisasterSolution;
import com.withergate.api.game.model.encounter.SolutionType;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.game.repository.action.DisasterActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.disaster.DisasterDetailsRepository;
import com.withergate.api.game.repository.disaster.DisasterRepository;
import com.withergate.api.game.repository.disaster.DisasterSolutionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.turn.TurnService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DisasterServiceTest {

    private DisasterServiceImpl disasterService;

    @Mock
    private DisasterRepository disasterRepository;

    @Mock
    private DisasterDetailsRepository disasterDetailsRepository;

    @Mock
    private DisasterSolutionRepository disasterSolutionRepository;

    @Mock
    private DisasterActionRepository disasterActionRepository;

    @Mock
    private DisasterResolutionService disasterResolutionService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private TurnService turnService;

    @Mock
    private RandomService randomService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EncounterService encounterService;

    @Mock
    private ItemService itemService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setDisasterVisibility(5);
        properties.setMaxTurns(45);
        properties.setDisasterTurns(List.of(15, 30, 45));

        disasterService = new DisasterServiceImpl(disasterRepository, disasterDetailsRepository, disasterSolutionRepository,
                disasterActionRepository, disasterResolutionService, clanRepository, characterService, turnService, randomService,
                notificationService, encounterService, itemService, properties);
    }

    @Test
    public void testGivenClanWithLowInformationWhenGettingDisasterThenVerifyNullReturned() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setInformationLevel(1);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        Disaster disaster = new Disaster();
        disaster.setId(1);
        disaster.setCompleted(false);
        disaster.setTurn(15);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        Turn turn = new Turn();
        turn.setTurnId(5);
        Mockito.when(turnService.getCurrentTurn()).thenReturn(turn);

        // when getting current disaster
        Disaster result = disasterService.getDisasterForClan(1);

        // then verify no disaster returned
        Assert.assertNull(result);
    }

    @Test
    public void testGivenClanWithHighInformationWhenGettingDisasterThenVerifyDisasterReturned() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setInformationLevel(5);
        Mockito.when(clanRepository.getOne(1)).thenReturn(clan);

        Disaster disaster = new Disaster();
        disaster.setId(1);
        disaster.setCompleted(false);
        disaster.setTurn(15);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        Turn turn = new Turn();
        turn.setTurnId(5);
        Mockito.when(turnService.getCurrentTurn()).thenReturn(turn);

        // when getting current disaster
        Disaster result = disasterService.getDisasterForClan(1);

        // then verify disaster returned
        Assert.assertEquals(disaster, result);
    }

    @Test
    public void testGivenNoActiveDisasterWhenHandlingDisasterThenVerifyDisasterPrepared() {
        // given no active disaster
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(null);
        Mockito.when(disasterRepository.findAll()).thenReturn(new ArrayList<>());

        DisasterDetails disasterDetails = new DisasterDetails();
        disasterDetails.setIdentifier("disaster");
        disasterDetails.setFinalDisaster(false);
        Mockito.when(disasterDetailsRepository.findAllByFinalDisaster(false)).thenReturn(List.of(disasterDetails));

        Mockito.when(randomService.getRandomInt(0, 0)).thenReturn(0); // random disaster list ID

        // when handling current disaster
        disasterService.handleDisaster(1);

        // then verify disaster prepared
        ArgumentCaptor<Disaster> captor = ArgumentCaptor.forClass(Disaster.class);
        Mockito.verify(disasterRepository).save(captor.capture());
        Assert.assertEquals("disaster", captor.getValue().getDetails().getIdentifier());
        Assert.assertFalse(captor.getValue().isCompleted());
        Assert.assertEquals(15, captor.getValue().getTurn());
    }

    @Test
    public void testGivenExistingDisasterWhenTurnsRemainingThenVerifyNothingDone() {
        // given disaster
        Disaster disaster = new Disaster();
        disaster.setCompleted(false);
        disaster.setTurn(15);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        // when handling disaster with lower turn
        disasterService.handleDisaster(5);

        // then verify nothing done
        Mockito.verify(disasterRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(disasterResolutionService, Mockito.never()).handleDisasterResolution(
                Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testGivenClanAndDisasterWhenHandlingDisasterThenVerifyResolutionServiceCalledAndDisasterUpdated() {
        // given clan and disaster
        Clan clan = new Clan();
        clan.setId(1);
        clan.setDisasterProgress(50);
        Mockito.when(clanRepository.findAll()).thenReturn(List.of(clan));

        DisasterDetails disasterDetails = new DisasterDetails();
        disasterDetails.setIdentifier("disaster");
        Disaster disaster = new Disaster();
        disaster.setTurn(15);
        disaster.setCompleted(false);
        disaster.setDetails(disasterDetails);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        // when handling disaster
        disasterService.handleDisaster(15);

        // then verify resolution service called and disaster updated
        Mockito.verify(disasterResolutionService).handleDisasterResolution(Mockito.eq(clan), Mockito.any(), Mockito.eq(disaster));
        Assert.assertTrue(disaster.isCompleted());
    }

    @Test
    public void testGivenAutomaticActionWhenProcessingActionsThenVerifyActionSucceeded() {
        // given action
        Character character = new Character();
        character.setName("John");
        character.setExperience(0);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setDisasterProgress(5);
        character.setClan(clan);

        DisasterSolution solution = new DisasterSolution();
        solution.setIdentifier("solution");
        solution.setSolutionType(SolutionType.AUTOMATIC);
        solution.setBonus(5);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        Mockito.when(encounterService.handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.AUTOMATIC), Mockito.anyInt(),
                Mockito.any())).thenReturn(true);

        // when processing actions
        disasterService.processDisasterActions(1);

        // then verify action succeeded
        Assert.assertEquals(10, clan.getDisasterProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Assert.assertEquals(2, character.getExperience());
    }

    @Test
    public void testGivenIntellectActionWithLowRollWhenProcessingActionsThenVerifyActionFailed() {
        // given action
        Character character = new Character();
        character.setName("John");
        character.setExperience(0);
        character.setIntellect(1);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setDisasterProgress(5);
        character.setClan(clan);

        DisasterSolution solution = new DisasterSolution();
        solution.setIdentifier("solution");
        solution.setSolutionType(SolutionType.INTELLECT);
        solution.setDifficulty(5);
        solution.setBonus(5);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(1); // low roll

        // when processing actions
        disasterService.processDisasterActions(1);

        // then verify action failed
        Assert.assertEquals(5, clan.getDisasterProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Assert.assertEquals(1, character.getExperience());
    }

    @Test
    public void testGivenIntellectActionWithHighRollWhenProcessingActionsThenVerifyActionSucceeded() {
        // given action
        Character character = new Character();
        character.setName("John");
        character.setExperience(0);
        character.setIntellect(3);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setDisasterProgress(5);
        character.setClan(clan);

        DisasterSolution solution = new DisasterSolution();
        solution.setIdentifier("solution");
        solution.setSolutionType(SolutionType.INTELLECT);
        solution.setDifficulty(5);
        solution.setBonus(5);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        Mockito.when(encounterService.handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.INTELLECT), Mockito.eq(5),
                Mockito.any())).thenReturn(true);

        // when processing actions
        disasterService.processDisasterActions(1);

        // then verify action succeeded
        Assert.assertEquals(10, clan.getDisasterProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Assert.assertEquals(2, character.getExperience());
    }

    @Test
    public void testGivenCraftsmanshipActionWithHighRollWhenProcessingActionsThenVerifyActionSucceeded() {
        // given action
        Character character = new Character();
        character.setName("John");
        character.setExperience(0);
        character.setCraftsmanship(3);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setDisasterProgress(5);
        character.setClan(clan);

        DisasterSolution solution = new DisasterSolution();
        solution.setIdentifier("solution");
        solution.setSolutionType(SolutionType.CRAFTSMANSHIP);
        solution.setDifficulty(5);
        solution.setBonus(5);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        Mockito.when(encounterService.handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.CRAFTSMANSHIP), Mockito.eq(5),
                Mockito.any())).thenReturn(true);

        // when processing actions
        disasterService.processDisasterActions(1);

        // then verify action succeeded
        Assert.assertEquals(10, clan.getDisasterProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Assert.assertEquals(2, character.getExperience());
    }

    @Test
    public void testGivenSuccessfulCombatActionWhenProcessingActionsThenVerifyActionSucceeded() {
        // given action
        Character character = new Character();
        character.setName("John");
        character.setExperience(0);
        character.setCombat(3);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setDisasterProgress(5);
        character.setClan(clan);

        DisasterSolution solution = new DisasterSolution();
        solution.setIdentifier("solution");
        solution.setSolutionType(SolutionType.COMBAT);
        solution.setDifficulty(5);
        solution.setBonus(5);

        Mockito.when(encounterService.handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.COMBAT), Mockito.eq(5),
                Mockito.any())).thenReturn(true);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        disasterService.processDisasterActions(1);

        // then verify action succeeded
        Assert.assertEquals(10, clan.getDisasterProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
        Assert.assertEquals(2, character.getExperience());
    }

}
