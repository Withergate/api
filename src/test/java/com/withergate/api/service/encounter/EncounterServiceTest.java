package com.withergate.api.service.encounter;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.encounter.Encounter;
import com.withergate.api.game.model.encounter.PenaltyType;
import com.withergate.api.game.model.encounter.RewardType;
import com.withergate.api.game.model.encounter.SolutionType;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.repository.EncounterRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class EncounterServiceTest {

    private EncounterServiceImpl encounterService;

    @Mock
    private EncounterRepository encounterRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private RandomService randomService;

    @Mock
    private CombatService combatService;

    @Mock
    private NotificationService notificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        encounterService = new EncounterServiceImpl(encounterRepository, itemService, randomService, combatService, notificationService);
    }

    @Test
    public void testGivenCombatEncounterWhenCharacterEncountersCombatThenVerifyCombatServiceCalled() {
        // given encounter
        Encounter encounter = new Encounter();
        encounter.setLocation(Location.CITY_CENTER);
        encounter.setType(SolutionType.COMBAT);
        encounter.setReward(RewardType.ITEM);
        encounter.setPenalty(PenaltyType.NONE);
        encounter.setDescriptionText("Combat encounter.");
        encounter.setSuccessText("Character won won!");
        encounter.setDifficulty(5);

        List<Encounter> encounters = new ArrayList<>();
        encounters.add(encounter);
        Mockito.when(encounterRepository.findAllByLocationAndTurnLessThan(Location.CITY_CENTER, 1)).thenReturn(encounters);

        Mockito.when(randomService.getRandomInt(0, 0)).thenReturn(0); // select the only encounter in the list

        Character character = new Character();
        character.setName("Rusty Nick");
        character.setCombat(2);

        ClanNotification notification = Mockito.mock(ClanNotification.class);

        // when character combats
        encounterService.handleEncounter(notification, character, Location.CITY_CENTER, 1);

        // then verify combat service called
        Mockito.verify(combatService).handleSingleCombat(notification, 5, character);
    }

    @Test
    public void testGivenIntellectEncounterWhenCharacterSucceedsThenVerifyRewardHandled() {
        // given encounter
        Encounter encounter = new Encounter();
        encounter.setLocation(Location.CITY_CENTER);
        encounter.setType(SolutionType.INTELLECT);
        encounter.setReward(RewardType.CAPS);
        encounter.setPenalty(PenaltyType.NONE);
        encounter.setDescriptionText("Intellect encounter.");
        encounter.setSuccessText("Character won!");
        encounter.setDifficulty(5);

        List<Encounter> encounters = new ArrayList<>();
        encounters.add(encounter);
        Mockito.when(encounterRepository.findAllByLocationAndTurnLessThan(Location.CITY_CENTER, 1)).thenReturn(encounters);

        Mockito.when(randomService.getRandomInt(0, 0)).thenReturn(0); // select the only encounter in the list

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");
        clan.setCaps(10);

        Character character = new Character();
        character.setName("Rusty Nick");
        character.setIntellect(4);
        character.setClan(clan);

        ClanNotification notification = Mockito.mock(ClanNotification.class);

        // when character succeeds with high roll followed by roll for caps
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(6, 3);

        encounterService.handleEncounter(notification, character, Location.CITY_CENTER, 1);

        // then verify clan updated
        assertEquals(16, clan.getCaps());
    }

    @Test
    public void testGivenIntellectEncounterWhenCharacterFailsThenVerifyPenaltyHandled() {
        // given encounter
        Encounter encounter = new Encounter();
        encounter.setLocation(Location.CITY_CENTER);
        encounter.setType(SolutionType.INTELLECT);
        encounter.setReward(RewardType.JUNK);
        encounter.setPenalty(PenaltyType.INJURY);
        encounter.setDescriptionText("Intellect encounter.");
        encounter.setFailureText("Character lost!");
        encounter.setDifficulty(10);

        List<Encounter> encounters = new ArrayList<>();
        encounters.add(encounter);
        Mockito.when(encounterRepository.findAllByLocationAndTurnLessThan(Location.CITY_CENTER, 1)).thenReturn(encounters);

        Mockito.when(randomService.getRandomInt(0, 0)).thenReturn(0); // select the only encounter in the list

        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Dragons");
        clan.setCaps(10);

        Character character = new Character();
        character.setName("Rusty Nick");
        character.setIntellect(4);
        character.setHitpoints(10);
        character.setClan(clan);

        ClanNotification notification = Mockito.mock(ClanNotification.class);

        // when character fails with low roll followed by roll for injury
        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(1, 3);

        encounterService.handleEncounter(notification, character, Location.CITY_CENTER, 1);

        // then verify notification updated
        assertEquals(7, character.getHitpoints());
    }

}
