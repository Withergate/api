package com.withergate.api.service.disaster;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.DisasterAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterDetails;
import com.withergate.api.model.disaster.DisasterSolution;
import com.withergate.api.model.disaster.DisasterSolution.Type;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.repository.action.DisasterActionRepository;
import com.withergate.api.repository.disaster.DisasterDetailsRepository;
import com.withergate.api.repository.disaster.DisasterRepository;
import com.withergate.api.repository.disaster.DisasterSolutionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.notification.NotificationService;
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
    private ClanService clanService;

    @Mock
    private TurnRepository turnRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private CombatService combatService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setDisasterVisibility(5);
        properties.setMaxTurns(45);
        properties.setDisasterTurns(List.of(15, 30, 45));

        disasterService = new DisasterServiceImpl(disasterRepository, disasterDetailsRepository, disasterSolutionRepository,
                disasterActionRepository, disasterResolutionService, clanService, turnRepository, randomService,
                notificationService, combatService, properties);
    }

    @Test
    public void testGivenCurrentDisasterWhenGettingDisasterThenVerifyRepositoryEntityReturned() {
        // given disaster
        Disaster disaster = new Disaster();
        disaster.setId(1);
        disaster.setCompleted(false);
        disaster.setTurn(15);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        // when getting current disaster
        Disaster result = disasterService.getCurrentDisaster();

        // then verify correct entity returned
        Assert.assertEquals(disaster, result);
    }

    @Test
    public void testGivenSolutionIdentifierWhenGettingSolutionThenVerifyRepositoryEntityReturned() {
        // given solution identifier
        String identifier = "solution";

        DisasterSolution solution = new DisasterSolution();
        solution.setIdentifier(identifier);
        solution.setDifficulty(5);
        Mockito.when(disasterSolutionRepository.getOne(identifier)).thenReturn(solution);

        // when getting solution
        DisasterSolution result = disasterService.getDisasterSolution(identifier);

        // then verify correct entity returned
        Assert.assertEquals(solution, result);
    }

    @Test
    public void testGivenDisasterActionWhenSavingActionThenVerifyEntitySaved() {
        // given action
        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(new Character());
        action.setSolution(new DisasterSolution());

        // when saving
        disasterService.saveDisasterAction(action);

        // then verify action saved
        Mockito.verify(disasterActionRepository).save(action);
    }

    @Test
    public void testGivenClanWithLowInformationWhenGettingDisasterThenVerifyNullReturned() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setInformationLevel(1);
        Mockito.when(clanService.getClan(1)).thenReturn(clan);

        Disaster disaster = new Disaster();
        disaster.setId(1);
        disaster.setCompleted(false);
        disaster.setTurn(15);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        Turn turn = new Turn();
        turn.setTurnId(5);
        Mockito.when(turnRepository.findFirstByOrderByTurnIdDesc()).thenReturn(turn);

        // when getting current disaster
        Disaster result = disasterService.getDisasterForClan(1);

        // then verify no disaster returned
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGivenClanWithHighInformationWhenGettingDisasterThenVerifyDisasterReturned() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setInformationLevel(5);
        Mockito.when(clanService.getClan(1)).thenReturn(clan);

        Disaster disaster = new Disaster();
        disaster.setId(1);
        disaster.setCompleted(false);
        disaster.setTurn(15);
        Mockito.when(disasterRepository.findFirstByCompleted(false)).thenReturn(disaster);

        Turn turn = new Turn();
        turn.setTurnId(5);
        Mockito.when(turnRepository.findFirstByOrderByTurnIdDesc()).thenReturn(turn);

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
        Assert.assertEquals(false, captor.getValue().isCompleted());
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
        Mockito.when(clanService.getAllClans()).thenReturn(List.of(clan));

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
        Assert.assertEquals(true, disaster.isCompleted());
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
        solution.setSolutionType(Type.AUTOMATIC);
        solution.setBonus(5);

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
        solution.setSolutionType(Type.INTELLECT);
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
        solution.setSolutionType(Type.INTELLECT);
        solution.setDifficulty(5);
        solution.setBonus(5);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(6); // high roll

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
        solution.setSolutionType(Type.CRAFTSMANSHIP);
        solution.setDifficulty(5);
        solution.setBonus(5);

        DisasterAction action = new DisasterAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setSolution(solution);
        Mockito.when(disasterActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(6); // high roll

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
        solution.setSolutionType(Type.COMBAT);
        solution.setDifficulty(5);
        solution.setBonus(5);

        Mockito.when(combatService.handleSingleCombat(Mockito.any(), Mockito.eq(5), Mockito.eq(character))).thenReturn(true);

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
