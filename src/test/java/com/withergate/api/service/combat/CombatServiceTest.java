package com.withergate.api.service.combat;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.combat.CombatResult;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CombatServiceTest {

    private CombatService combatService;

    @Mock
    private CombatRoundService combatRoundService;

    @Mock
    private RandomService randomService;

    @Mock
    private CharacterService characterService;

    @Mock
    private NotificationService notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        combatService = new CombatServiceImpl(combatRoundService, randomService, characterService, notificationService);
    }

    @Test
    public void testGivenCharacterWhenHandlingSingleCombatCombatThenReturnTrueIfCombatWon() {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setName("John");

        CombatResult combatResult = new CombatResult();
        combatResult.setWinner(character);
        combatResult.setLoser(new Character());
        combatResult.setFinished(true);
        combatResult.setDetails(new ArrayList<>());
        Mockito.when(combatRoundService.handleCombatRound(Mockito.eq(character), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(combatResult);

        // when handling single combat
        boolean result = combatService.handleSingleCombat(new ClanNotification(), 5, character);

        // then return true
        Assert.assertTrue(result);
    }

    @Test
    public void testGivenCharactersWhenHandlingArenaThenVerifyAllResultsReturned() {
        // given characters
        Clan clan1 = new Clan();
        clan1.setId(1);
        Character character1 = new Character();
        character1.setId(1);
        character1.setName("John");
        character1.setClan(clan1);

        Clan clan2 = new Clan();
        clan2.setId(2);
        Character character2 = new Character();
        character2.setId(2);
        character2.setName("Jane");
        character2.setClan(clan2);

        List<Character> characters = List.of(character1, character2);

        CombatResult combatResult = new CombatResult();
        combatResult.setWinner(character1);
        combatResult.setLoser(character2);
        combatResult.setFinished(true);
        combatResult.setDetails(new ArrayList<>());
        Mockito.when(combatRoundService.handleCombatRound(Mockito.eq(character1), Mockito.any(), Mockito.eq(character2), Mockito.any()))
                .thenReturn(combatResult);

        // when handling arena fights
        List<ArenaResult> results = combatService.handleArenaFights(characters);

        // then verify correct number of results returned
        Assert.assertEquals(2, results.size());
    }

}
