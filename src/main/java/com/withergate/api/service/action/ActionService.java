package com.withergate.api.service.action;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.WeaponType;
import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.request.LocationRequest;
import com.withergate.api.service.building.IBuildingService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.IClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.ILocationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LocationAction service. Responsible for the execution of all location-bound actions.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class ActionService implements IActionService {

    private final CharacterService characterService;
    private final ILocationService locationService;
    private final IClanService clanService;
    private final GameProperties gameProperties;
    private final IBuildingService buildingService;

    public ActionService(CharacterService characterService, ILocationService locationService, IClanService clanService,
                         GameProperties gameProperties, IBuildingService buildingService) {
        this.characterService = characterService;
        this.locationService = locationService;
        this.clanService = clanService;
        this.gameProperties = gameProperties;
        this.buildingService = buildingService;
    }

    @Transactional
    @Override
    public void createLocationAction(LocationRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting location action request: {}", request.toString());
        Character character = getCharacter(request.getCharacterId(), clanId);

        // check if clan has enough resources
        if (request.getLocation() == Location.TAVERN) {
            Clan clan = character.getClan();
            if (clan.getCaps() < gameProperties.getCharacterCost()) {
                throw new InvalidActionException("Not enough resources to perform this action!");
            }
            clan.setCaps(clan.getCaps() - gameProperties.getCharacterCost());
            clanService.saveClan(clan);
        }

        // check arena requirements
        if (request.getLocation() == Location.ARENA) {
            Clan clan = character.getClan();
            if (clan.isArena()) {
                throw new InvalidActionException("You already have selected a character to enter arena this turn!");
            }
            if (character.getWeapon() != null && character.getWeapon().getDetails().getType() != WeaponType.MELEE) {
                throw new InvalidActionException("Only melee weapons are allowed to the arena!");
            }
            clan.setArena(true);
            clanService.saveClan(clan);
        }

        LocationAction action = new LocationAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setLocation(request.getLocation());

        locationService.saveLocationAction(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
        characterService.save(character);
    }

    @Transactional
    @Override
    public void createBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting building action for request {}.", request);
        Character character = getCharacter(request.getCharacterId(), clanId);

        // check if action is applicable
        BuildingDetails buildingDetails = buildingService.getBuildingDetails(request.getBuilding());
        if (buildingDetails == null) {
            throw new InvalidActionException("This building does not exist");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !character.getClan().getBuildings()
                .containsKey(request.getBuilding())) {
            throw new InvalidActionException("This building is not constructed yet!");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !character.getClan().getBuildings()
                .get(request.getBuilding()).getDetails().isVisitable()) {
            throw new InvalidActionException("This building does not support this type of action.");

        }

        if (request.getType() == BuildingAction.Type.CONSTRUCT && character.getClan().getJunk() < character
                .getCraftsmanship()) {
            throw new InvalidActionException("Not enough junk to perform this action.");
        }

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(buildingDetails.getIdentifier());
        action.setType(request.getType());

        buildingService.saveBuildingAction(action);

        // pay junk
        Clan clan = character.getClan();
        clan.setJunk(clan.getJunk() - character.getCraftsmanship());
        clanService.saveClan(clan);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
        characterService.save(character);

    }

    @Transactional
    @Override
    public void processLocationActions(int turnId) {
        // arena actions
        locationService.processArenaActions(turnId);

        // location actions
        locationService.processLocationActions(turnId);
    }

    @Transactional
    @Override
    public void processBuildingActions(int turnId) {
        // passive building bonuses
        buildingService.processPassiveBuildingBonuses(turnId);

        // building actions
        buildingService.processBuildingActions(turnId);
    }

    private Character getCharacter(int characterId, int clanId) throws InvalidActionException {
        Character character = characterService.load(characterId);
        if (character == null || character.getClan().getId() != clanId
                || character.getState() != CharacterState.READY) {
            log.error("Action cannot be performed with this character: {}!", character);
            throw new InvalidActionException("Cannot perform exploration with the specified character!");
        }

        return character;
    }

}
