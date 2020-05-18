package com.withergate.api.service.faction;

import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.FactionAction;
import com.withergate.api.game.model.action.FactionAction.Type;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.model.faction.FactionAid;
import com.withergate.api.game.model.request.FactionRequest;
import com.withergate.api.game.repository.action.FactionActionRepository;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.faction.FactionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class FactionServiceTest {

    private FactionService factionService;

    @Mock
    private FactionRepository factionRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private FactionActionRepository factionActionRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RandomService randomService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private QuestService questService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setFactionEntryFame(50);

        factionService = new FactionServiceImpl(factionRepository, characterService, factionActionRepository, notificationService,
                randomService, clanRepository, itemService, questService, properties);
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenJoinRequestWhenClanInFactionThenVerifyExceptionThrown() throws Exception {
        // given action
        Faction faction = new Faction();
        faction.setIdentifier("FACTION");
        Mockito.when(factionRepository.getOne("FACTION")).thenReturn(faction);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFaction(faction);
        clan.setFame(50);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        FactionRequest request = new FactionRequest();
        request.setCharacterId(1);
        request.setFaction("FACTION");
        request.setType(Type.JOIN);

        // when submitting action
        factionService.saveFactionAction(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenJoinRequestWhenClanHasLowFameThenVerifyExceptionThrown() throws Exception {
        // given action
        Faction faction = new Faction();
        faction.setIdentifier("FACTION");
        Mockito.when(factionRepository.getOne("FACTION")).thenReturn(faction);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFame(10);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        FactionRequest request = new FactionRequest();
        request.setCharacterId(1);
        request.setFaction("FACTION");
        request.setType(Type.JOIN);

        // when submitting action
        factionService.saveFactionAction(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenJoinRequestWhenClanHasHighFameThenVerifyActionSaved() throws Exception {
        // given action
        Faction faction = new Faction();
        faction.setIdentifier("FACTION");
        Mockito.when(factionRepository.getOne("FACTION")).thenReturn(faction);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFame(100);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        FactionRequest request = new FactionRequest();
        request.setCharacterId(1);
        request.setFaction("FACTION");
        request.setType(Type.JOIN);

        // when submitting action
        factionService.saveFactionAction(request, 1);

        // then verify action saved
        ArgumentCaptor<FactionAction> captor = ArgumentCaptor.forClass(FactionAction.class);
        Mockito.verify(factionActionRepository).save(captor.capture());
        Assert.assertEquals(ActionState.PENDING, captor.getValue().getState());
        Assert.assertEquals("FACTION", captor.getValue().getFaction());
        Assert.assertEquals(character, captor.getValue().getCharacter());
    }

    @Test
    public void testGivenFactionActionWhenJoiningThenVerifyFactionAssigned() {
        // given action
        Faction faction = new Faction();
        faction.setIdentifier("FACTION");
        Mockito.when(factionRepository.getOne("FACTION")).thenReturn(faction);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setFame(100);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);

        FactionAction action = new FactionAction();
        action.setState(ActionState.PENDING);
        action.setType(Type.JOIN);
        action.setCharacter(character);
        action.setFaction("FACTION");
        Mockito.when(factionActionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        // when processing actions
        factionService.runActions(1);

        // then verify faction assigned
        Assert.assertEquals(faction, clan.getFaction());
        Assert.assertEquals(ActionState.COMPLETED, action.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenSupportRequestWhenClanHasLowCapsThenVerifyExceptionThrown() throws Exception {
        // given action
        Faction faction = new Faction();
        faction.setIdentifier("FACTION");

        FactionAid aid = new FactionAid();
        aid.setIdentifier("aid");
        aid.setCost(10);
        aid.setFaction(faction);
        faction.setFactionAids(List.of(aid));
        Mockito.when(factionRepository.getOne("FACTION")).thenReturn(faction);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(5);
        clan.setFaction(faction);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        FactionRequest request = new FactionRequest();
        request.setCharacterId(1);
        request.setFaction("FACTION");
        request.setType(Type.SUPPORT);
        request.setFactionAid("aid");

        // when submitting action
        factionService.saveFactionAction(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenSupportRequestWhenClanHasSufficientCapsThenVerifyActionSaved() throws Exception {
        // given action
        Faction faction = new Faction();
        faction.setIdentifier("FACTION");

        FactionAid aid = new FactionAid();
        aid.setIdentifier("aid");
        aid.setCost(10);
        aid.setFaction(faction);
        faction.setFactionAids(List.of(aid));
        Mockito.when(factionRepository.getOne("FACTION")).thenReturn(faction);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(15);
        clan.setFaction(faction);

        Character character = new Character();
        character.setId(1);
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        FactionRequest request = new FactionRequest();
        request.setCharacterId(1);
        request.setFaction("FACTION");
        request.setType(Type.SUPPORT);
        request.setFactionAid("aid");

        // when submitting action
        factionService.saveFactionAction(request, 1);

        // then verify action saved
        ArgumentCaptor<FactionAction> captor = ArgumentCaptor.forClass(FactionAction.class);
        Mockito.verify(factionActionRepository).save(captor.capture());
        Assert.assertEquals(ActionState.PENDING, captor.getValue().getState());
        Assert.assertEquals(character, captor.getValue().getCharacter());
        Assert.assertEquals(aid, captor.getValue().getFactionAid());
        Assert.assertEquals(5, clan.getCaps());
    }

}
