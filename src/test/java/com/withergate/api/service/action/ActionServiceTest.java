package com.withergate.api.service.action;

import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.combat.ClanCombatService;
import com.withergate.api.service.disaster.DisasterService;
import com.withergate.api.service.faction.FactionService;
import com.withergate.api.service.item.CraftingService;
import com.withergate.api.service.location.ArenaService;
import com.withergate.api.service.location.LocationService;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.research.ResearchService;
import com.withergate.api.service.trade.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ActionServiceTest {

    private ActionServiceImpl actionService;

    @Mock
    private CharacterService characterService;

    @Mock
    private BuildingService buildingService;

    @Mock
    private CraftingService craftingService;

    @Mock
    private ResearchService researchService;

    @Mock
    private LocationService locationService;

    @Mock
    private QuestService questService;

    @Mock
    private TradeService tradeService;

    @Mock
    private ArenaService arenaService;

    @Mock
    private TavernService tavernService;

    @Mock
    private DisasterService disasterService;

    @Mock
    private FactionService factionService;

    @Mock
    private ClanCombatService clanCombatService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        actionService = new ActionServiceImpl(characterService, locationService, buildingService, craftingService, researchService,
                questService, tradeService, arenaService, tavernService, disasterService, factionService, clanCombatService);
    }

    @Test
    public void testGivenTurnIdWhenProcessingLocationActionsThenVerifyAllProcessedTriggered() {
        // given turn ID
        int turnId = 1;

        // when processing location actions
        actionService.processLocationActions(turnId);

        // then verify all processed triggered
        Mockito.verify(locationService).processLocationActions(turnId);
        Mockito.verify(arenaService).processArenaActions(turnId);
    }

    @Test
    public void testGivenTurnIdWhenProcessingBuildingActionsThenVerifyAllProcessedTriggered() {
        // given turn ID
        int turnId = 1;

        // when processing location actions
        actionService.processBuildingActions(turnId);

        // then verify all processed triggered
        Mockito.verify(buildingService).processBuildingActions(turnId);
    }

}
