package com.withergate.api.service.building;

import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.action.BuildingAction.Type;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.request.BuildingRequest;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails.ResearchName;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.BonusUtils;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Building service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class BuildingServiceImpl implements BuildingService {

    private final ItemService itemService;
    private final BuildingActionRepository buildingActionRepository;
    private final BuildingDetailsRepository buildingDetailsRepository;
    private final NotificationService notificationService;
    private final RandomService randomService;
    private final GameProperties gameProperties;
    private final CharacterService characterService;

    @Transactional
    @Override
    public void saveBuildingAction(BuildingRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting building action for request {}.", request);
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);

        // check if action is applicable
        Building building = character.getClan().getBuildings().get(request.getBuilding());
        if (building == null) {
            throw new InvalidActionException("This building does not exist");
        }

        if (request.getType() == BuildingAction.Type.VISIT && building.getLevel() < 1) {
            throw new InvalidActionException("This building has not reached sufficient level yet!!");
        }

        if (request.getType() == BuildingAction.Type.VISIT && !building.getDetails().isVisitable()) {
            throw new InvalidActionException("This building does not support this type of action.");
        }

        if (request.getType() == BuildingAction.Type.VISIT && character.getClan().getJunk() < building.getVisitJunkCost()) {
            throw new InvalidActionException("Not enough junk to perform this action!");
        }

        if (request.getType() == BuildingAction.Type.CONSTRUCT && character.getClan().getJunk() < character.getCraftsmanship()) {
            throw new InvalidActionException("Not enough junk to perform this action.");
        }

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(building.getDetails().getIdentifier());
        action.setType(request.getType());

        buildingActionRepository.save(action);

        // pay junk
        Clan clan = character.getClan();
        if (request.getType().equals(BuildingAction.Type.CONSTRUCT)) {
            clan.changeJunk(- character.getCraftsmanship());
        } else if (request.getType().equals(BuildingAction.Type.VISIT)) {
            clan.changeJunk(- building.getVisitJunkCost());
        }

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
    }

    @Override
    public void processBuildingActions(int turnId) {
        log.debug("Processing building actions...");

        for (BuildingAction action : buildingActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            Character character = action.getCharacter();
            Clan clan = character.getClan();

            // construct actions
            if (action.getType() == BuildingAction.Type.CONSTRUCT) {
                processConstructAction(action, character, clan, notification);
            }

            // visit actions
            if (action.getType().equals(Type.VISIT)) {
                processVisitAction(action, character, clan, notification);
            }

            // award experience
            character.changeExperience(1);
            notification.changeExperience(1);

            // dismiss action
            action.setState(ActionState.COMPLETED);

            // save notification
            notificationService.save(notification);
        }
    }

    @Override
    public void processPassiveBuildingBonuses(int turnId, Clan clan) {
        log.debug("Processing passive building bonuses for clan {}.", clan.getId());

        // monument
        if (clan.getBuildings().get(BuildingName.MONUMENT).getLevel() > 0) {
            Building building = clan.getBuildings().get(BuildingName.MONUMENT);
            clan.changeFame(building.getLevel());

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.changeFame(building.getLevel());

            notificationService.addLocalizedTexts(notification.getText(), "building.monument.income", new String[] {});
            notificationService.save(notification);
        }

        // GMO farm
        if (clan.getBuildings().get(BuildingDetails.BuildingName.GMO_FARM).getLevel() > 0) {
            Building building = clan.getBuildings().get(BuildingDetails.BuildingName.GMO_FARM);
            clan.changeFood(building.getLevel() * 2);

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.changeFood(building.getLevel() * 2);
            notificationService.addLocalizedTexts(notification.getText(), "building.gmofarm.income", new String[] {});
            notificationService.save(notification);
        }

        // Training grounds
        if (clan.getBuildings().get(BuildingDetails.BuildingName.TRAINING_GROUNDS).getLevel() > 0) {
            Building building = clan.getBuildings().get(BuildingDetails.BuildingName.TRAINING_GROUNDS);
            for (Character character : clan.getCharacters()) {
                if (character.getState().equals(CharacterState.RESTING)) {
                    character.changeExperience(building.getLevel());

                    ClanNotification notification = new ClanNotification(turnId, clan.getId());
                    notification.setHeader(character.getName());
                    notification.setImageUrl(character.getImageUrl());
                    notification.changeExperience(building.getLevel());
                    notificationService.addLocalizedTexts(notification.getText(), "building.traininggrounds.income", new String[] {});
                    notificationService.save(notification);
                }
            }
        }

        // Watchtower
        if (clan.getBuildings().get(BuildingName.STUDY).getLevel() > 0) {
            Building building = clan.getBuildings().get(BuildingName.STUDY);
            clan.changeInformation(building.getLevel());

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.changeInformation(building.getLevel());
            notificationService.addLocalizedTexts(notification.getText(), "building.study.income", new String[] {});
            notificationService.save(notification);
        }
    }

    @Override
    public BuildingDetails getBuildingDetails(BuildingDetails.BuildingName name) {
        return buildingDetailsRepository.getOne(name);
    }

    @Override
    public List<BuildingDetails> getAllBuildingDetails() {
        return buildingDetailsRepository.findAll();
    }

    private void processConstructAction(BuildingAction action, Character character, Clan clan, ClanNotification notification) {
        BuildingDetails details = getBuildingDetails(action.getBuilding());

        int progress = character.getCraftsmanship(); // progress to be done

        // calculate bonus for gear and traits
        progress += getBonus(character, notification, BonusType.CONSTRUCT);

        notificationService.addLocalizedTexts(notification.getText(), "building.work", new String[] {}, details.getName());

        // progress
        Building building = clan.getBuildings().get(action.getBuilding());
        building.setProgress(building.getProgress() + progress);
        if (building.getProgress() >= building.getNextLevelWork()) {
            building.setProgress(building.getProgress() - building.getNextLevelWork());
            building.setLevel(building.getLevel() + 1);

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.building.levelup", new String[] {}, details.getName());
            notification.getDetails().add(detail);

            // level up bonuses
            processBuildingLevelUpBonuses(building, clan, notification);
        }
    }

    private void processVisitAction(BuildingAction action, Character character, Clan clan, ClanNotification notification) {
        if (action.getBuilding().equals(BuildingName.FORGE)) {
            log.debug("{} is crafting weapon.", character.getName());
            notificationService.addLocalizedTexts(notification.getText(), "building.crafting.weapon", new String[] {character.getName()});
            itemService.generateCraftableItem(character, clan.getBuildings().get(BuildingDetails.BuildingName.FORGE).getLevel(),
                    getBonus(character, notification, BonusType.CRAFTING), notification, ItemType.WEAPON);

        }

        if (action.getBuilding().equals(BuildingName.RAGS_SHOP)) {
            log.debug("{} is crafting outfit.", character.getName());
            notificationService.addLocalizedTexts(notification.getText(), "building.crafting.outfit", new String[] {character.getName()});
            itemService.generateCraftableItem(character, clan.getBuildings().get(BuildingName.RAGS_SHOP).getLevel(),
                    getBonus(character, notification, BonusType.CRAFTING),notification, ItemType.OUTFIT);
        }

        if (action.getBuilding().equals(BuildingName.WORKSHOP)) {
            log.debug("{} is crafting gear.", character.getName());
            notificationService.addLocalizedTexts(notification.getText(), "building.crafting.gear", new String[] {character.getName()});
            itemService.generateCraftableItem(character, clan.getBuildings().get(BuildingName.WORKSHOP).getLevel(),
                    getBonus(character, notification, BonusType.CRAFTING), notification, ItemType.GEAR);
        }
    }

    private int getBonus(Character character, ClanNotification notification, BonusType bonusType) {
        int bonus = 0;

        bonus += BonusUtils.getTraitBonus(character, bonusType, notification, notificationService);
        bonus += BonusUtils.getItemBonus(character, bonusType, notification, notificationService);

        // research side effect
        if (bonusType.equals(BonusType.CRAFTING) && character.getClan().getResearch().containsKey(ResearchName.FORGERY)
                && character.getClan().getResearch().get(ResearchName.FORGERY).isCompleted()) {
            // add caps to clan for forgery
            int caps = randomService.getRandomInt(1, RandomServiceImpl.K4);
            character.getClan().changeCaps(caps);
            notification.changeCaps(caps);

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.research.forgery", new String[]{});
            notification.getDetails().add(detail);
        }

        return bonus;
    }

    private void processBuildingLevelUpBonuses(Building building, Clan clan, ClanNotification notification) {
        // award fame
        int fame = gameProperties.getBuildingFame() * building.getLevel();
        notification.changeFame(fame);
        clan.changeFame(fame);

        Research architecture = clan.getResearch().get(ResearchName.ARCHITECTURE);
        if (architecture != null && architecture.isCompleted()) {
            // add fame to clan for architecture
            notification.changeFame(architecture.getDetails().getValue());
            clan.changeFame(architecture.getDetails().getValue());

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.research.architecture", new String[]{});
            notification.getDetails().add(detail);
        }
    }
}
