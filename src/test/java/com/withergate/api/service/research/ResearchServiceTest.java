package com.withergate.api.service.research;

import java.util.List;

import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResearchAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails;
import com.withergate.api.repository.action.ResearchActionRepository;
import com.withergate.api.repository.research.ResearchDetailsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ResearchServiceTest {

    private ResearchService researchService;

    @Mock
    private ResearchDetailsRepository detailsRepository;

    @Mock
    private ResearchActionRepository actionRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private CharacterService characterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        researchService = new ResearchServiceImpl(detailsRepository, actionRepository, notificationService, characterService);
    }

    @Test
    public void testGivenClanWhenAssigningResearchThenVerifyResearchAssigned() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);

        ResearchDetails details1 = new ResearchDetails();
        details1.setIdentifier("BEGGING");
        ResearchDetails details2 = new ResearchDetails();
        details2.setIdentifier("ARCHITECTURE");
        List<ResearchDetails> details = List.of(details1, details2);
        Mockito.when(detailsRepository.findAll()).thenReturn(details);

        // when assigning research
        researchService.assignResearch(clan);

        // then verify research assigned
        Assert.assertNotNull(clan.getResearch("BEGGING"));
        Assert.assertNotNull(clan.getResearch("ARCHITECTURE"));
    }

    @Test
    public void testGivenResearchActionWhenProcessingActionsThenVerifyProgressIncreased() {
        // given action
        ResearchDetails details = new ResearchDetails();
        details.setIdentifier("BEGGING");
        details.setCost(20);

        Research research = new Research();
        research.setProgress(2);
        research.setDetails(details);

        Clan clan = new Clan();
        clan.getResearch().add(research);

        Character character = new Character();
        character.setIntellect(5);
        character.setClan(clan);

        ResearchAction action = new ResearchAction();
        action.setResearch("BEGGING");
        action.setCharacter(character);
        action.setState(ActionState.PENDING);

        Mockito.when(actionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        researchService.processResearchActions(1);

        // then verify progress increased
        Assert.assertEquals(7, research.getProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenResearchActionWhenProcessingActionsWithTraitThenVerifyProgressIncreased() {
        // given action
        ResearchDetails details = new ResearchDetails();
        details.setIdentifier("BEGGING");
        details.setCost(20);

        Research research = new Research();
        research.setProgress(2);
        research.setDetails(details);

        Clan clan = new Clan();
        clan.getResearch().add(research);

        Character character = new Character();
        character.setIntellect(5);
        character.setClan(clan);

        TraitDetails traitDetails = new TraitDetails();
        traitDetails.setBonus(2);
        traitDetails.setIdentifier("BOFFIN");
        traitDetails.setBonusType(BonusType.RESEARCH);
        Trait trait = new Trait();
        trait.setActive(true);
        trait.setDetails(traitDetails);
        character.getTraits().add(trait);

        ResearchAction action = new ResearchAction();
        action.setResearch("BEGGING");
        action.setCharacter(character);
        action.setState(ActionState.PENDING);

        Mockito.when(actionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        researchService.processResearchActions(1);

        // then verify progress increased
        Assert.assertEquals(9, research.getProgress());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test
    public void testGivenResearchActionWhenCompletingResearchThenVerifyCompletionHandled() {
        // given action
        ResearchDetails details = new ResearchDetails();
        details.setIdentifier("BEGGING");
        details.setCost(20);
        details.setFame(5);

        Research research = new Research();
        research.setProgress(18);
        research.setDetails(details);

        Clan clan = new Clan();
        clan.setFame(5);
        clan.getResearch().add(research);

        Character character = new Character();
        character.setIntellect(5);
        character.setClan(clan);

        ResearchAction action = new ResearchAction();
        action.setResearch("BEGGING");
        action.setCharacter(character);
        action.setState(ActionState.PENDING);

        Mockito.when(actionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        researchService.processResearchActions(1);

        // then verify completion handled
        Assert.assertEquals(20, research.getProgress());
        Assert.assertTrue(research.isCompleted());
        Assert.assertEquals(10, clan.getFame());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

}
