package com.withergate.api.service.quest;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.QuestAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.quest.Quest;
import com.withergate.api.model.quest.QuestDetails;
import com.withergate.api.model.quest.QuestDetails.Type;
import com.withergate.api.repository.action.QuestActionRepository;
import com.withergate.api.repository.quest.QuestDetailsRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.CombatService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class QuestServiceTest {

    private QuestServiceImpl questService;

    @Mock
    private QuestDetailsRepository questDetailsRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private QuestActionRepository questActionRepository;

    @Mock
    private CombatService combatService;

    @Mock
    private ClanService clanService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        questService = new QuestServiceImpl(questDetailsRepository, notificationService, questActionRepository, combatService, clanService);
    }

    @Test
    public void testGivenClanWhenAssigningQuestsThenVerifyAllQuestsAdded() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setQuests(new ArrayList<>());

        List<QuestDetails> detailsList = new ArrayList<>();
        QuestDetails details = new QuestDetails();
        details.setIdentifier("Quest");
        details.setType(Type.COMBAT);
        detailsList.add(details);
        Mockito.when(questDetailsRepository.findAllByInformationLevel(1)).thenReturn(detailsList);

        // when assigning quests
        ClanNotification notification = new ClanNotification();
        questService.assignQuests(clan, notification, 1);

        // then verify quests added
        Assert.assertEquals(details, clan.getQuests().get(0).getDetails());
    }

    @Test
    public void testGivenQuestActionWhenSavingActionThenVerifyActionSaved() {
        // given quest action
        QuestAction action = new QuestAction();

        Character character = new Character();
        character.setName("Character");
        Quest quest = new Quest();
        quest.setId(1);

        action.setCharacter(character);
        action.setQuest(quest);

        // when saving action
        questService.saveQuestAction(action);

        // then verify action saved
        Mockito.verify(questActionRepository).save(action);
    }

}
