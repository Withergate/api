package com.withergate.api.service.combat;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.combat.CombatResult;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CombatRoundServiceTest {

    private CombatRoundService combatRoundService;

    @Mock
    private RandomService randomService;

    @Mock
    private NotificationService notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        combatRoundService = new CombatRoundServiceImpl(randomService, notificationService);
    }

    @Test
    public void testGivenTwoCharactersWhenHandlingCombatRoundThenVerifyLoserInjured() {
        // given two characters
        Character character1 = new Character();
        character1.setName("John");
        character1.setHitpoints(10);
        character1.setMaxHitpoints(10);
        character1.setCombat(5);

        Character character2 = new Character();
        character2.setName("Mike");
        character2.setHitpoints(10);
        character2.setMaxHitpoints(10);
        character2.setCombat(3);

        // when handling combat
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(4, 2); // combat rolls
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(99); // flee roll

        CombatResult result = combatRoundService.handleCombatRound(character1, new ClanNotification(), character2, new ClanNotification());

        // then verify loser lost hitpoints
        Assert.assertEquals(character1, result.getWinner());
        Assert.assertEquals(character2, result.getLoser());
        Assert.assertEquals(6, character2.getHitpoints());
        Assert.assertFalse(result.isFinished());
    }

    @Test
    public void testGivenTwoCharactersWhenHandlingCombatRoundWithLowFleeRollThenVerifyCombatEnded() {
        // given two characters
        Character character1 = new Character();
        character1.setName("John");
        character1.setHitpoints(10);
        character1.setMaxHitpoints(10);
        character1.setCombat(5);

        Character character2 = new Character();
        character2.setName("Mike");
        character2.setHitpoints(10);
        character2.setMaxHitpoints(10);
        character2.setCombat(1);

        // when handling combat
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(4, 2); // combat rolls
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K100)).thenReturn(1); // flee roll

        CombatResult result = combatRoundService.handleCombatRound(character1, new ClanNotification(), character2, new ClanNotification());

        // then verify loser fleed
        Assert.assertEquals(character1, result.getWinner());
        Assert.assertEquals(character2, result.getLoser());
        Assert.assertTrue(result.isFinished());
    }

    @Test
    public void testGivenTwoCharactersWhenHandlingCombatRoundWithDeathThenVerifyCombatEnded() {
        // given two characters
        Character character1 = new Character();
        character1.setName("John");
        character1.setHitpoints(1);
        character1.setMaxHitpoints(10);
        character1.setCombat(1);
        character1.setClan(new Clan());

        Character character2 = new Character();
        character2.setName("Mike");
        character2.setHitpoints(10);
        character2.setMaxHitpoints(10);
        character2.setCombat(5);
        character2.setClan(new Clan());

        // when handling combat
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(4, 4); // combat rolls

        CombatResult result = combatRoundService.handleCombatRound(character1, new ClanNotification(), character2, new ClanNotification());

        // then verify loser died
        Assert.assertEquals(character2, result.getWinner());
        Assert.assertEquals(character1, result.getLoser());
        Assert.assertTrue(result.isFinished());
    }

}
