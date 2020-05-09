package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.character.TraitDetails;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ClanTurnServiceTest {

    private ClanTurnService clanTurnService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private CharacterService characterService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private QuestService questService;

    @Mock
    private BuildingService buildingService;

    @Mock
    private TavernService tavernService;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setHealing(2);
        properties.setFoodConsumption(2);
        properties.setStarvationInjury(2);
        properties.setStarvationFame(1);

        clanTurnService = new ClanTurnServiceImpl(clanRepository, characterService, notificationService, questService,
                buildingService, tavernService, randomService, properties);
    }

    @Test
    public void testGivenClanWhenIncreasingInformationLevelThenVerifyQuestServiceCalled() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");
        clan.setInformationLevel(0);
        clan.setInformation(14);

        // when increasing information level
        clanTurnService.performClanTurnUpdates(clan, 1);

        // then verify quest service called
        Mockito.verify(questService).assignQuests(Mockito.eq(clan), Mockito.any(ClanNotification.class), Mockito.eq(1));
        Assert.assertEquals(1, clan.getInformationLevel());
        Assert.assertEquals(2, clan.getInformation());
    }

    @Test
    public void testGivenClanWhenPerformingTurnActionsThenVerifyFoodConsumedAndBuildingsTriggered() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);

        Character character1 = new Character();
        character1.setId(1);
        character1.setName("John");
        character1.setClan(clan);
        character1.setHitpoints(1);
        clan.getCharacters().add(character1);

        Character character2 = new Character();
        character2.setId(2);
        character2.setName("Jane");
        character2.setHitpoints(1);
        character2.setClan(clan);
        clan.getCharacters().add(character2);

        // Ascetic
        Character character3 = new Character();
        character3.setId(3);
        character3.setName("Josh");
        character3.setClan(clan);
        character3.setHitpoints(1);
        TraitDetails details = new TraitDetails();
        details.setIdentifier("ASCETIC");
        details.setBonus(1);
        details.setBonusType(BonusType.FOOD_CONSUMPTION);
        Trait trait = new Trait();
        trait.setDetails(details);
        trait.setActive(true);
        character3.getTraits().add(trait);
        clan.getCharacters().add(character3);

        // when performing turn updates
        clanTurnService.performClanTurnUpdates(clan, 1);

        // then verify food consumed and buildings triggered
        Assert.assertEquals(5, clan.getFood());
        Mockito.verify(buildingService).processPassiveBuildingBonuses(1, clan);
    }

    @Test
    public void testGivenReadyCharacterWhenPerformingEndTurnUpdatesThenVerifyCharacterHealed() {
        // given character
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(10);

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(5);
        character.setMaxHitpoints(7);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);
        clan.getCharacters().add(character);

        // when performing healing
        clanTurnService.performClanTurnUpdates(clan,1);

        // then verify character updated
        assertEquals(7, character.getHitpoints());
    }

    @Test
    public void testGivenReadyCharacterWithFullHitpointsWhenPerformingEndTurnUpdatesThenVerifyCharacterNotHealed() {
        // given character
        Clan clan = new Clan();
        clan.setId(1);

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(7);
        character.setMaxHitpoints(7);
        character.setName("Rusty Nick");
        character.setState(CharacterState.READY);
        character.setClan(clan);

        // when performing healing
        clanTurnService.performClanTurnUpdates(clan,1);

        // then verify character not updated
        assertEquals(7, character.getHitpoints());
    }

    @Test
    public void testGivenClanWithoutFoodWhenPerformingTurnActionsThenVerifyCharacterStarving() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFood(0);
        clan.setFame(2);

        Character character = new Character();
        character.setId(1);
        character.setHitpoints(5);
        character.setClan(clan);
        clan.getCharacters().add(character);

        // when performing turn updates
        clanTurnService.performClanTurnUpdates(clan,1);

        // then verify character starving and fame lost
        Assert.assertEquals(3, character.getHitpoints());
        Assert.assertEquals(1, clan.getFame());
    }

}
