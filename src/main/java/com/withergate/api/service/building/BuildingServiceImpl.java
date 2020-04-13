package com.withergate.api.service.building;

import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.BuildingAction;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.BuildingRequest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.repository.action.BuildingActionRepository;
import com.withergate.api.game.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.utils.BonusUtils;
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
        Building building = character.getClan().getBuilding(request.getBuilding());
        if (building == null) {
            throw new InvalidActionException("This building does not exist");
        }

        if (character.getClan().getJunk() < character.getCraftsmanship()) {
            throw new InvalidActionException("Not enough junk to perform this action.");
        }

        BuildingAction action = new BuildingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setBuilding(building.getDetails().getIdentifier());

        buildingActionRepository.save(action);

        // pay junk
        Clan clan = character.getClan();
        clan.changeJunk(- character.getCraftsmanship());

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
            processConstructAction(action, character, clan, notification);


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

        // fame
        ClanNotification notificationFame = new ClanNotification(turnId, clan.getId());
        notificationFame.setHeader(clan.getName());
        int fame = BonusUtils.getBuildingEndBonus(clan, PassiveBonusType.FAME_INCOME, notificationService, notificationFame);
        if (fame > 0) {
            clan.changeFame(fame);
            notificationFame.changeFame(fame);
            notificationService.save(notificationFame);
        }

        // food
        ClanNotification notificationFood = new ClanNotification(turnId, clan.getId());
        notificationFood.setHeader(clan.getName());
        int food = BonusUtils.getBuildingEndBonus(clan, PassiveBonusType.FOOD_INCOME, notificationService, notificationFood);
        if (food > 0) {
            clan.changeFood(food);
            notificationFood.changeFood(food);
            notificationService.save(notificationFood);
        }

        // information
        ClanNotification notificationInfo = new ClanNotification(turnId, clan.getId());
        notificationInfo.setHeader(clan.getName());
        int info = BonusUtils.getBuildingEndBonus(clan, PassiveBonusType.INFORMATION_INCOME, notificationService, notificationInfo);
        if (info > 0) {
            clan.changeInformation(info);
            notificationInfo.changeInformation(info);
            notificationService.save(notificationInfo);
        }
    }

    @Override
    public BuildingDetails getBuildingDetails(String name) {
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
        progress += BonusUtils.getBonus(character, BonusType.CONSTRUCT, notification, notificationService);

        notificationService.addLocalizedTexts(notification.getText(), "building.work", new String[] {}, details.getName());

        // progress
        Building building = clan.getBuilding(action.getBuilding());
        building.setProgress(building.getProgress() + progress);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.action.progress",
                new String[]{building.getProgress() + " / " + building.getNextLevelWork()});
        notification.getDetails().add(detail);

        if (building.getProgress() >= building.getNextLevelWork()) {
            building.setProgress(building.getProgress() - building.getNextLevelWork());
            building.setLevel(building.getLevel() + 1);

            NotificationDetail detailLevel = new NotificationDetail();
            notificationService.addLocalizedTexts(detailLevel.getText(), "detail.building.levelup", new String[] {}, details.getName());
            notification.getDetails().add(detailLevel);

            // level up bonuses
            processBuildingLevelUpBonuses(building, clan, notification);
        }
    }

    private void processBuildingLevelUpBonuses(Building building, Clan clan, ClanNotification notification) {
        // award fame
        int fame = gameProperties.getBuildingFame() * building.getLevel();
        notification.changeFame(fame);
        clan.changeFame(fame);

        Research research = clan.getResearch(ResearchBonusType.BUILDING_FAME);
        if (research != null && research.isCompleted()) {
            // add fame to clan for architecture
            notification.changeFame(research.getDetails().getValue());
            clan.changeFame(research.getDetails().getValue());

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), research.getDetails().getBonusText(), new String[]{});
            notification.getDetails().add(detail);
        }
    }
}
