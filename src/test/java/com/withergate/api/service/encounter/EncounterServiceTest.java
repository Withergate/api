package com.withergate.api.service.encounter;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.encounter.EncounterType;
import com.withergate.api.model.encounter.RewardType;
import com.withergate.api.repository.EncounterRepository;
import com.withergate.api.service.IRandomService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.IItemService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class EncounterServiceTest {

    private EncounterService encounterService;

    @Mock
    private EncounterRepository encounterRepository;

    @Mock
    private IItemService itemService;

    @Mock
    private IRandomService randomService;

    @Mock
    private ICombatService combatService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        encounterService = new EncounterService(encounterRepository, itemService, randomService, combatService);
    }

    @Test
    public void testGivenCombatEncounterWhenCharacterEncountersCombatThenVerifyCombatServiceCalled() {
        // given encounter
        Encounter encounter = new Encounter();
        encounter.setType(EncounterType.COMBAT);
        encounter.setReward(RewardType.ITEM);
        encounter.setDescriptionText("Combat encounter.");
        encounter.setSuccessText("[CH] won!");
        encounter.setDifficulty(5);

        List<Encounter> encounters = new ArrayList<>();
        encounters.add(encounter);
        Mockito.when(encounterRepository.findAll()).thenReturn(encounters);

        Mockito.when(randomService.getRandomInt(0, 0)).thenReturn(0); // select the only encounter in the list

        Character character = new Character();
        character.setName("Rusty Nick");
        character.setCombat(2);

        ClanNotification notification = Mockito.mock(ClanNotification.class);

        // when character combats
        encounterService.handleEncounter(notification, character, Location.CITY);

        // then verify notification updated
        Mockito.verify(combatService).handleCombat(notification, encounter, character, Location.CITY);
    }
}
