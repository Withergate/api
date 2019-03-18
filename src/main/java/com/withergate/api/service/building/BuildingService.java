package com.withergate.api.service.building;

import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.BuildingAction;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.repository.ClanNotificationRepository;
import com.withergate.api.repository.action.BuildingActionRepository;
import com.withergate.api.repository.building.BuildingDetailsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Building service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class BuildingService implements IBuildingService {

    private final CharacterService characterService;
    private final ClanService clanService;
    private final BuildingActionRepository buildingActionRepository;
    private final BuildingDetailsRepository buildingDetailsRepository;
    private final ClanNotificationRepository notificationRepository;

    /**
     * Constructor.
     *
     * @param characterService          character service
     * @param clanService               clan service
     * @param buildingActionRepository  building action repository
     * @param buildingDetailsRepository building details repository
     * @param notificationRepository    notification repository
     */
    public BuildingService(CharacterService characterService, ClanService clanService,
                           BuildingActionRepository buildingActionRepository,
                           BuildingDetailsRepository buildingDetailsRepository,
                           ClanNotificationRepository notificationRepository) {
        this.characterService = characterService;
        this.clanService = clanService;
        this.buildingActionRepository = buildingActionRepository;
        this.buildingDetailsRepository = buildingDetailsRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    @Override
    public void processBuildingActions(int turnId) {
        log.debug("Processing building actions...");

        for (BuildingAction action : buildingActionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification();
            notification.setClanId(action.getCharacter().getClan().getId());
            notification.setTurnId(turnId);

            Character character = action.getCharacter();
            Clan clan = character.getClan();
            BuildingDetails details = getBuildingDetails(action.getBuilding());
            if (action.getType() == BuildingAction.Type.CONSTRUCT) {
                int progress = character.getCraftsmanship(); // progress to be done

                notification.setText("[" + character.getName() + "] worked on building " + details.getName() + ".");

                if (clan.getBuildings().containsKey(action.getBuilding())) {
                    log.debug("Improving existing building {}.", action.getBuilding());

                    // progress
                    Building building = clan.getBuildings().get(action.getBuilding());
                    building.setProgress(building.getProgress() + progress);
                    if (building.getProgress() >= building.getNextLevelWork()) {
                        building.setProgress(building.getProgress() - building.getNextLevelWork());
                        building.setLevel(building.getLevel() + 1);

                        notification.setDetails("[" + details.getName() + "] advanced to the next level!");
                    }

                } else {
                    log.debug("Constructing new building: {}.", details.getIdentifier());

                    Building building = new Building();
                    building.setDetails(details);
                    building.setClan(clan);
                    building.setLevel(0);
                    building.setProgress(progress);
                    clan.getBuildings().put(details.getIdentifier(), building);

                    notification.setText("[" + character.getName() + "] worked on building " + details.getName() + ".");
                }
            }

            // TODO: implement visit actions

            // make character ready again and award experience
            character.setState(CharacterState.READY);
            character.setExperience(character.getExperience() + 1);
            notification.setExperience(1);
            characterService.save(character);

            // dismiss action
            action.setState(ActionState.COMPLETED);
            buildingActionRepository.save(action);

            // save notification
            notificationRepository.save(notification);
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
                    notification.setFameIncome(building.getLevel());
                    notification.setText("Your monument generated " + building.getLevel() + " this turn.");
                    notificationRepository.save(notification);

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
