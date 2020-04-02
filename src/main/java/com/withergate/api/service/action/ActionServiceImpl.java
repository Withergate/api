package com.withergate.api.service.action;

import java.util.Optional;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.LocationAction.LocationActionType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.disaster.DisasterSolution;
import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.request.DisasterRequest;
import com.withergate.api.game.model.request.LocationRequest;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.combat.ClanCombatService;
import com.withergate.api.service.disaster.DisasterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.faction.FactionService;
import com.withergate.api.service.item.CraftingService;
import com.withergate.api.service.location.ArenaService;
import com.withergate.api.service.location.LocationService;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.research.ResearchService;
import com.withergate.api.service.trade.TradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Action service. Responsible for the execution of all game actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class ActionServiceImpl implements ActionService {

    private final CharacterService characterService;
    private final LocationService locationService;
    private final BuildingService buildingService;
    private final CraftingService craftingService;
    private final ResearchService researchService;
    private final QuestService questService;
    private final TradeService tradeService;
    private final ArenaService arenaService;
    private final TavernService tavernService;
    private final DisasterService disasterService;
    private final FactionService factionService;
    private final ClanCombatService clanCombatService;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processClanCombatActions(int turnId) {
        log.debug("-> Processing clan combat actions...");

        clanCombatService.processClanCombatActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processLocationActions(int turnId) {
        log.debug("-> Processing location actions...");

        // arena actions
        arenaService.processArenaActions(turnId);

        // location actions
        locationService.processLocationActions(turnId);

        // tavern actions
        tavernService.processTavernActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processBuildingActions(int turnId) {
        log.debug("-> Processing building actions...");

        // building actions
        buildingService.processBuildingActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processCraftingActions(int turnId) {
        log.debug("-> Processing crafting actions...");

        // crafting actions
        craftingService.processCraftingActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processResearchActions(int turnId) {
        log.debug("-> Processing research actions");

        // research actions
        researchService.processResearchActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processQuestActions(int turnId) {
        log.debug("-> Processing quest actions...");

        // quest actions
        questService.processQuestActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void processTradeActions(int turnId) {
        log.debug("-> Processing trade actions...");

        // resource trade actions
        tradeService.processResourceTradeActions(turnId);

        // market trade actions
        tradeService.processMarketTradeActions(turnId);

        // perform computer trade actions
        tradeService.performComputerTradeActions(turnId);

        // prepare offers for next turn
        tradeService.prepareComputerMarketOffers();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Retryable
    @Override
    public void processDisaster(int turnId) {
        log.debug("-> Processing disaster actions...");

        // process disaster actions
        disasterService.processDisasterActions(turnId);

        // process disaster
        disasterService.handleDisaster(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Retryable
    @Override
    public void processFactionActions(int turnId) {
        log.debug("-> Processing faction actions...");

        // process faction actions
        factionService.processFactionActions(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
    @Retryable
    @Override
    public void performEndGameActions(int turnId) {
        log.debug("-> Processing end game actions...");

        // factions fame distribution
        factionService.handleFameDistribution(turnId);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void assignDefaultActions() {
        log.debug("Assigning default actions");

        for (Character character : characterService.loadAll()) {
            // skip characters without clan
            if (character.getClan() == null) {
                continue;
            }

            // only applicable to ready characters
            if (!character.getState().equals(CharacterState.READY)) {
                continue;
            }

            // handle disaster first
            if (character.getClan().isPreferDisaster()) {
                Disaster disaster = disasterService.getDisasterForClan(character.getClan().getId());

                if (disaster != null && character.getClan().getDisasterProgress() < 100) {
                    Optional<DisasterSolution> solution = disaster.getDetails().getSolutions().stream().filter(DisasterSolution::isBasic)
                            .findFirst();
                    if (solution.isPresent()) {
                        DisasterRequest request = new DisasterRequest();
                        request.setCharacterId(character.getId());
                        request.setSolution(solution.get().getIdentifier());
                        try {
                            disasterService.saveDisasterAction(request, character.getClan().getId());
                            continue;
                        } catch (InvalidActionException e) {
                            log.error("Error assigning default action.", e);
                        }
                    }
                }
            }

            // exploration
            if (character.getClan().getDefaultAction().equals(Clan.DefaultAction.EXPLORE_NEIGHBORHOOD)) {
                LocationRequest request = new LocationRequest();
                request.setLocation(Location.NEIGHBORHOOD);
                request.setType(LocationActionType.VISIT);
                request.setCharacterId(character.getId());
                try {
                    locationService.saveLocationAction(request, character.getClan().getId());
                } catch (InvalidActionException e) {
                    log.error("Error assigning default action.", e);
                }
            } else {
                character.setState(CharacterState.RESTING);
            }
        }
    }

    private Character getCharacter(int characterId, int clanId) throws InvalidActionException {
        Character character = characterService.load(characterId);
        if (character == null || character.getClan().getId() != clanId || character.getState() != CharacterState.READY) {
            log.error("Action cannot be performed with this character: {}!", character);
            throw new InvalidActionException("Cannot perform exploration with the specified character!");
        }

        return character;
    }

}
