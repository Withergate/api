package com.withergate.api.service.building;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Building service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class BuildingServiceImpl implements BuildingService {

    private final CharacterService characterService;
    private final ClanService clanService;
    private final BuildingActionRepository buildingActionRepository;
    private final BuildingDetailsRepository buildingDetailsRepository;
    private final NotificationService notificationService;

    public BuildingServiceImpl(CharacterService characterService, ClanService clanService,
                               BuildingActionRepository buildingActionRepository,
                               BuildingDetailsRepository buildingDetailsRepository,
                               NotificationService notificationService) {
        this.characterService = characterService;
        this.clanService = clanService;
        this.buildingActionRepository = buildingActionRepository;
        this.buildingDetailsRepository = buildingDetailsRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void saveBuildingAction(BuildingAction action) {
        buildingActionRepository.save(action);
    }

    @Transactional
    @Override
    public void processBuildingActions(int turnId) {
        log.debug("Processing building actions...");

        for (BuildingAction action : buildingActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification();
            notification.setClanId(action.getCharacter().getClan().getId());
            notification.setTurnId(turnId);
            notification.setHeader(action.getCharacter().getName());

            Character character = action.getCharacter();
            Clan clan = character.getClan();
            BuildingDetails details = getBuildingDetails(action.getBuilding());
            if (action.getType() == BuildingAction.Type.CONSTRUCT) {
                int progress = character.getCraftsmanship(); // progress to be done

                // builder trait
                if (character.getTraits().containsKey(TraitDetails.TraitName.BUILDER)) {
                    progress = progress + 2;
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.trait.builder", new String[]{character.getName()});
                    notification.getDetails().add(detail);
                }

                notificationService.addLocalizedTexts(notification.getText(), "building.work", new String[]{});

                if (clan.getBuildings().containsKey(action.getBuilding())) {
                    log.debug("Improving existing building {}.", action.getBuilding());

                    // progress
                    Building building = clan.getBuildings().get(action.getBuilding());
                    building.setProgress(building.getProgress() + progress);
                    if (building.getProgress() >= building.getNextLevelWork()) {
                        building.setProgress(building.getProgress() - building.getNextLevelWork());
                        building.setLevel(building.getLevel() + 1);

                        NotificationDetail detail = new NotificationDetail();
                        notificationService.addLocalizedTexts(detail.getText(), "detail.building.levelup", new String[]{});
                        notification.getDetails().add(detail);
                    }

                } else {
                    log.debug("Constructing new building: {}.", details.getIdentifier());

                    Building building = new Building();
                    building.setDetails(details);
                    building.setClan(clan);
                    building.setLevel(0);
                    building.setProgress(progress);
                    clan.getBuildings().put(details.getIdentifier(), building);
                }
            }

            // TODO: implement visit actions

            // award experience
            character.setExperience(character.getExperience() + 1);
            notification.setExperience(1);
            characterService.save(character);

            // dismiss action
            action.setState(ActionState.COMPLETED);
            buildingActionRepository.save(action);

            // save notification
            notificationService.save(notification);
        }
    }

    @Transactional
    @Override
    public void processPassiveBuildingBonuses(int turnId) {
        log.debug("Processing passive building bonuses");

        for (Clan clan : clanService.getAllClans()) {
            // monument
            if (clan.getBuildings().containsKey(BuildingDetails.BuildingName.MONUMENT)) {
                Building building = clan.getBuildings().get(BuildingDetails.BuildingName.MONUMENT);
                if (building.getLevel() > 0) {
                    clan.setFame(clan.getFame() + building.getLevel());
                    clanService.saveClan(clan);

                    ClanNotification notification = new ClanNotification();
                    notification.setTurnId(turnId);
                    notification.setClanId(clan.getId());
                    notification.setHeader(clan.getName());
                    notification.setFameIncome(building.getLevel());

                    notificationService.addLocalizedTexts(notification.getText(), "building.monument.income", new String[]{});
                    notificationService.save(notification);
                }
            }

            // GMO farm
            if (clan.getBuildings().containsKey(BuildingDetails.BuildingName.GMO_FARM)) {
                Building building = clan.getBuildings().get(BuildingDetails.BuildingName.GMO_FARM);
                if (building.getLevel() > 0) {
                    clan.setFood(clan.getFood() + building.getLevel());
                    clanService.saveClan(clan);

                    ClanNotification notification = new ClanNotification();
                    notification.setTurnId(turnId);
                    notification.setClanId(clan.getId());
                    notification.setHeader(clan.getName());
                    notification.setFoodIncome(building.getLevel());
                    notificationService.addLocalizedTexts(notification.getText(), "building.gmofarm.income", new String[]{});
                    notificationService.save(notification);

                }
            }

            // Training grounds
            if (clan.getBuildings().containsKey(BuildingDetails.BuildingName.TRAINING_GROUNDS)) {
                Building building = clan.getBuildings().get(BuildingDetails.BuildingName.TRAINING_GROUNDS);
                if (building.getLevel() > 0) {
                    for (Character character : clan.getCharacters()) {
                        if (character.getState() == CharacterState.READY) {
                            character.setExperience(character.getExperience() + building.getLevel());
                            characterService.save(character);

                            ClanNotification notification = new ClanNotification();
                            notification.setTurnId(turnId);
                            notification.setClanId(clan.getId());
                            notification.setHeader(character.getName());
                            notification.setExperience(building.getLevel());
                            notificationService.addLocalizedTexts(notification.getText(), "building.traininggrounds.income", new String[]{});
                            notificationService.save(notification);
                        }
                    }
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
}
