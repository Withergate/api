package com.withergate.api.service.building;

import java.util.List;

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
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public void saveBuildingAction(BuildingAction action) {
        buildingActionRepository.save(action);
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
            character.setExperience(character.getExperience() + 1);
            notification.setExperience(1);

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
            clan.setFame(clan.getFame() + building.getLevel());

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.setFameIncome(building.getLevel());

            notificationService.addLocalizedTexts(notification.getText(), "building.monument.income", new String[] {});
            notificationService.save(notification);
        }

        // GMO farm
        if (clan.getBuildings().get(BuildingDetails.BuildingName.GMO_FARM).getLevel() > 0) {
            Building building = clan.getBuildings().get(BuildingDetails.BuildingName.GMO_FARM);
            clan.setFood(clan.getFood() + building.getLevel() * 2);

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.setFoodIncome(building.getLevel() * 2);
            notificationService.addLocalizedTexts(notification.getText(), "building.gmofarm.income", new String[] {});
            notificationService.save(notification);
        }

        // Training grounds
        if (clan.getBuildings().get(BuildingDetails.BuildingName.TRAINING_GROUNDS).getLevel() > 0) {
            Building building = clan.getBuildings().get(BuildingDetails.BuildingName.TRAINING_GROUNDS);
            for (Character character : clan.getCharacters()) {
                if (character.getState() == CharacterState.READY) {
                    character.setExperience(character.getExperience() + building.getLevel());

                    ClanNotification notification = new ClanNotification(turnId, clan.getId());
                    notification.setHeader(character.getName());
                    notification.setExperience(building.getLevel());
                    notificationService.addLocalizedTexts(notification.getText(), "building.traininggrounds.income", new String[] {});
                    notificationService.save(notification);
                }
            }

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
        }
    }

    private void processVisitAction(BuildingAction action, Character character, Clan clan, ClanNotification notification) {
        if (action.getBuilding().equals(BuildingName.FORGE)) {

            log.debug("{} is crafting weapon.", character.getName());
            notificationService.addLocalizedTexts(notification.getText(), "building.crafting.weapon", new String[] {character.getName()});
            itemService.generateCraftableItem(character, clan.getBuildings().get(BuildingDetails.BuildingName.FORGE).getLevel(),
                    notification, ItemType.WEAPON);

        }

        if (action.getBuilding().equals(BuildingName.RAGS_SHOP)) {
            log.debug("{} is crafting outfit.", character.getName());
            notificationService.addLocalizedTexts(notification.getText(), "building.crafting.outfit", new String[] {character.getName()});
            itemService.generateCraftableItem(character, clan.getBuildings().get(BuildingName.RAGS_SHOP).getLevel(), notification,
                    ItemType.OUTFIT);
        }

        if (action.getBuilding().equals(BuildingName.WORKSHOP)) {
            log.debug("{} is crafting gear.", character.getName());
            notificationService.addLocalizedTexts(notification.getText(), "building.crafting.gear", new String[] {character.getName()});
            itemService.generateCraftableItem(character, clan.getBuildings().get(BuildingName.WORKSHOP).getLevel(), notification,
                    ItemType.GEAR);
        }
    }

    private int getBonus(Character character, ClanNotification notification, BonusType bonusType) {
        int bonus = 0;

        if (bonusType.equals(BonusType.CONSTRUCT) && character.getTraits().containsKey(TraitDetails.TraitName.BUILDER)) {
            bonus += character.getTraits().get(TraitName.BUILDER).getDetails().getBonus();
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.trait.builder", new String[] {character.getName()});
            notification.getDetails().add(detail);
        }

        Gear gear = character.getGear();
        if (bonusType.equals(BonusType.CONSTRUCT) && gear != null && gear.getDetails().getBonusType().equals(BonusType.CONSTRUCT)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "gear.bonus.work", new String[] {}, gear.getDetails().getName());
            notification.getDetails().add(detail);

            bonus += character.getGear().getDetails().getBonus();
        }

        if (bonusType.equals(BonusType.FORGE) && gear != null && gear.getDetails().getBonusType().equals(BonusType.FORGE)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "gear.bonus.work", new String[] {}, gear.getDetails().getName());
            notification.getDetails().add(detail);

            bonus += character.getGear().getDetails().getBonus();
        }

        return bonus;
    }
}
