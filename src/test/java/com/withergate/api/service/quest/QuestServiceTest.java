package com.withergate.api.service.quest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.QuestAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Gender;
import com.withergate.api.game.model.encounter.SolutionCondition;
import com.withergate.api.game.model.encounter.SolutionType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.quest.Quest;
import com.withergate.api.game.model.quest.QuestDetails;
import com.withergate.api.game.model.request.QuestRequest;
import com.withergate.api.game.repository.action.QuestActionRepository;
import com.withergate.api.game.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.encounter.EncounterService;
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

public class QuestServiceTest {

    private QuestServiceImpl questService;

    @Mock
    private QuestDetailsRepository questDetailsRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private QuestActionRepository questActionRepository;

    @Mock
    private RandomService randomService;

    @Mock
    private CharacterService characterService;

    @Mock
    private EncounterService encounterService;

    @Mock
    private ItemService itemService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        questService = new QuestServiceImpl(questDetailsRepository, notificationService, questActionRepository, randomService,
                characterService, encounterService, itemService);
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenQuestRequestWhenNoQuestsForClanThenVerifyExceptionThrown() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        // when creating quest action
        questService.saveQuestAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenQuestRequestWhenQuestAlreadyCompletedThenVerifyExceptionThrown() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Quest quest = new Quest();
        quest.setId(1);
        quest.setCompleted(true);
        clan.getQuests().add(quest);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        // when creating quest action
        questService.saveQuestAction(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenQuestRequestWhenQuestAvailableThenVerifyActionSaved() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Quest quest = new Quest();
        quest.setId(1);
        quest.setCompleted(false);
        quest.setDetails(new QuestDetails());
        clan.getQuests().add(quest);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        // when creating building action
        questService.saveQuestAction(request, 1);

        // then verify action saved
        ArgumentCaptor<QuestAction> captor = ArgumentCaptor.forClass(QuestAction.class);
        Mockito.verify(questActionRepository).save(captor.capture());
        assertEquals(character, captor.getValue().getCharacter());
        assertEquals(quest, captor.getValue().getQuest());
    }

    @Test
    public void testGivenClanWhenAssigningQuestsThenVerifyAllQuestsAdded() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setQuests(new HashSet<>());

        List<QuestDetails> detailsList = new ArrayList<>();
        QuestDetails details = new QuestDetails();
        details.setIdentifier("Quest");
        details.setType(SolutionType.COMBAT);
        detailsList.add(details);
        Mockito.when(questDetailsRepository.findAllByTurnLessThan(1)).thenReturn(detailsList);

        // when assigning quests
        ClanNotification notification = new ClanNotification();
        questService.assignQuests(clan, notification, 1);

        // then verify quests added
        Assert.assertEquals(1, clan.getQuests().toArray().length);
    }

    @Test
    public void testGivenQuestActionWhenSuccessThenVerifyEntitiesUpdated() {
        // given quest action
        Character character = new Character();
        character.setId(1);
        character.setIntellect(5);
        character.setExperience(0);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        QuestDetails details = new QuestDetails();
        details.setType(SolutionType.INTELLECT);
        details.setDifficulty(1);
        details.setCompletion(4);

        Quest quest = new Quest();
        quest.setCompleted(false);
        quest.setProgress(0);
        quest.setDetails(details);
        quest.setClan(clan);

        QuestAction action = new QuestAction();
        action.setQuest(quest);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        List<QuestAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(questActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when succeeding in quest action
        Mockito.when(encounterService.handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.INTELLECT), Mockito.eq(1),
                Mockito.any())).thenReturn(true);

        questService.runActions(1);

        // then verify entities updated
        Assert.assertEquals(1, quest.getProgress());
        Assert.assertEquals(2, character.getExperience());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenQuestActionWhenSuccessInProgressedQuestThenVerifyEntitiesUpdated() {
        // given quest action
        Character character = new Character();
        character.setId(1);
        character.setCraftsmanship(5);
        character.setExperience(0);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFame(10);
        clan.setCaps(10);
        character.setClan(clan);

        QuestDetails details = new QuestDetails();
        details.setType(SolutionType.CRAFTSMANSHIP);
        details.setDifficulty(1);
        details.setCompletion(4);
        details.setFameReward(20);
        details.setCapsReward(10);

        Quest quest = new Quest();
        quest.setCompleted(false);
        quest.setProgress(3);
        quest.setDetails(details);
        quest.setClan(clan);

        QuestAction action = new QuestAction();
        action.setQuest(quest);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        List<QuestAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(questActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when succeeding in quest action
        Mockito.when(encounterService.handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.CRAFTSMANSHIP), Mockito.eq(1),
                Mockito.any())).thenReturn(true);

        questService.runActions(1);

        // then verify entities updated
        Assert.assertTrue(quest.isCompleted());
        Assert.assertEquals(2, character.getExperience());
        Assert.assertEquals(30, clan.getFame());
        Assert.assertEquals(20, clan.getCaps());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenQuestActionWhenHandlingCombatThenVerifyCombatServiceCalled() {
        // given quest action
        Character character = new Character();
        character.setId(1);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFame(10);
        clan.setCaps(10);
        character.setClan(clan);

        QuestDetails details = new QuestDetails();
        details.setType(SolutionType.COMBAT);
        details.setDifficulty(1);
        details.setCompletion(4);

        Quest quest = new Quest();
        quest.setCompleted(false);
        quest.setProgress(0);
        quest.setDetails(details);
        quest.setClan(clan);

        QuestAction action = new QuestAction();
        action.setQuest(quest);
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        List<QuestAction> actions = new ArrayList<>();
        actions.add(action);
        Mockito.when(questActionRepository.findAllByState(ActionState.PENDING)).thenReturn(actions);

        // when handling actions
        questService.runActions(1);

        // then verify combat service called
        Mockito.verify(encounterService).handleSolution(Mockito.eq(character), Mockito.eq(SolutionType.COMBAT), Mockito.eq(1), Mockito.any(ClanNotification.class));
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenQuestRequestWhenConditionNotMetThenVerifyExceptionThrown() throws InvalidActionException {
        // given quest request
        QuestRequest request = new QuestRequest();
        request.setCharacterId(1);
        request.setQuestId(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);
        clan.setQuests(new HashSet<>());
        clan.setName("Dragons");

        Quest quest = new Quest();
        quest.setId(1);
        quest.setCompleted(false);
        QuestDetails details = new QuestDetails();
        details.setCondition(SolutionCondition.FEMALE_CHARACTER);
        quest.setDetails(details);
        clan.getQuests().add(quest);

        Character character = new Character();
        character.setId(1);
        character.setName("Rusty Nick");
        character.setGender(Gender.MALE);
        character.setClan(clan);
        character.setState(CharacterState.READY);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        // when creating building action
        questService.saveQuestAction(request, 1);

        // then verify exception thrown
    }

}
