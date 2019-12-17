package com.withergate.api.service.clan;

import java.util.Iterator;

import com.withergate.api.GameProperties;
import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails.ResearchName;
import com.withergate.api.model.statistics.ClanTurnStatistics;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.repository.statistics.ClanTurnStatisticsRepository;
import com.withergate.api.service.BonusUtils;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Clan turn service implementation.
 *
 * @author Martin Myslik
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ClanTurnServiceImpl implements ClanTurnService {

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final NotificationService notificationService;
    private final QuestService questService;
    private final BuildingService buildingService;
    private final TavernService tavernService;
    private final RandomService randomService;
    private final ClanTurnStatisticsRepository statisticsRepository;
    private final GameProperties gameProperties;

    @Override
    public void performClanTurnUpdates(Clan clan, int turnId) {
        log.debug("-> Performing turn updates for clan {}.", clan.getId());

        // delete dead characters
        deleteDeadCharacters(clan);

        // passive buildings
        buildingService.processPassiveBuildingBonuses(turnId, clan);

        // tavern offers
        tavernService.prepareTavernOffers(clan);

        // food consumption
        performFoodConsumption(turnId, clan);

        // perform resting
        performResting(turnId, clan);

        // perform character leveling
        performCharacterLeveling(turnId, clan);

        // check information level
        checkInformationLevel(turnId, clan);

        // perform end-turn research updates
        performResearchEndTurnUpdates(turnId, clan);

        // mark characters as ready
        markCharactersReady(clan);

        // probably redundant
        clanRepository.saveAndFlush(clan);

        // prepare statistics
        prepareStatistics(turnId, clan);
    }

    private void deleteDeadCharacters(Clan clan) {
        Iterator<Character> iterator = clan.getCharacters().iterator();
        while (iterator.hasNext()) {
            Character character = iterator.next();
            if (character.getHitpoints() < 1) {
                log.debug("Deleting character {}.", character.getName());
                iterator.remove();
                character.getClan().getCharacters().remove(character);

                characterService.delete(character);
            }
        }
    }

    private void performFoodConsumption(int turnId, Clan clan) {
        log.debug("Food consumption for clan {}.", clan.getId());

        if (clan.getCharacters().isEmpty()) {
            return;
        }

        ClanNotification notification = new ClanNotification(turnId, clan.getId());
        notification.setHeader(clan.getName());
        notificationService.addLocalizedTexts(notification.getText(), "clan.foodConsumption", new String[] {});

        Iterator<Character> iterator = clan.getCharacters().iterator();
        while (iterator.hasNext()) {
            Character character = iterator.next();
            int consumption = gameProperties.getFoodConsumption();

            // ascetic
            consumption -= BonusUtils.getTraitBonus(character, BonusType.FOOD_CONSUMPTION, notification, notificationService);

            if (clan.getFood() >= consumption) {
                clan.setFood(clan.getFood() - consumption);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.foodConsumption",
                        new String[] {character.getName()});
                notification.getDetails().add(detail);
                notification.changeFood(- consumption);
            } else {
                log.debug("Character {} is starving,", character.getName());

                character.changeHitpoints(- gameProperties.getStarvationInjury());
                character.setState(CharacterState.STARVING);

                // lose fame
                clan.changeFame(- gameProperties.getStarvationFame());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.starving", new String[] {character.getName()});
                notification.getDetails().add(detail);
                notification.changeFame(- gameProperties.getStarvationFame());
                notification.changeInjury(gameProperties.getStarvationInjury());

                if (character.getHitpoints() < 1) {
                    log.debug("Character {} died of starvation.", character.getName());

                    NotificationDetail detailDeath = new NotificationDetail();
                    notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.starvationdeath",
                            new String[] {character.getName()});
                    notification.getDetails().add(detailDeath);
                    notification.setDeath(true);

                    // delete and remove from clan
                    characterService.delete(character);
                    iterator.remove();
                }
            }
        }

        notificationService.save(notification);
    }

    private void performResting(int turnId, Clan clan) {
        for (Character character : clan.getCharacters()) {
            // heal only resting characters
            if (!(character.getState().equals(CharacterState.READY) || character.getState().equals(CharacterState.RESTING))) {
                continue;
            }

            // skip npc characters
            if (character.getClan() == null) {
                continue;
            }

            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
            notification.setHeader(character.getName());
            notification.setImageUrl(character.getImageUrl());

            // handle resting bonuses
            handleRestingBonuses(character, notification);

            handleHealing(character, notification);

            // discard empty notification
            if (notification.getDetails().size() > 0 || !notification.getText().isEmpty()) {
                notificationService.save(notification);
            }

        }
    }

    private void performCharacterLeveling(int turnId, Clan clan) {
        for (Character character : clan.getCharacters()) {
            if (character.getExperience() >= character.getNextLevelExperience()) {
                // level up
                characterService.increaseCharacterLevel(character, turnId);
            }
        }
    }

    private void checkInformationLevel(int turnId, Clan clan) {
        log.debug("Clan {} has information level {}.", clan.getId(), clan.getInformationLevel());

        // handle next level
        if (clan.getInformation() >= clan.getNextLevelInformation()) {
            log.debug("Increasing clan's information level.");

            clan.changeInformation(- clan.getNextLevelInformation());
            clan.setInformationLevel(clan.getInformationLevel() + 1);

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notificationService.addLocalizedTexts(notification.getText(), "clan.information.levelup", new String[] {});

            // assign quests
            questService.assignQuests(clan, notification);

            // notify about new research
            notifyAvailableResearch(clan, notification);

            // save notification
            notificationService.save(notification);
        }
    }

    private void performResearchEndTurnUpdates(int turnId, Clan clan) {
        log.debug("Performing end turn research updates");

        Research culinary = clan.getResearch().get(ResearchName.CULINARY);
        if (culinary != null && culinary.isCompleted()) {
            int fame = clan.getFood() / culinary.getDetails().getValue();
            if (fame > 0) {
                ClanNotification notification = new ClanNotification(turnId, clan.getId());
                notification.setHeader(clan.getName());

                clan.changeFame(fame);
                notification.changeFame(fame);

                notificationService.addLocalizedTexts(notification.getText(), "research.culinary", new String[]{});
                notificationService.save(notification);
            }
        }

        Research decoration = clan.getResearch().get(ResearchName.DECORATION);
        if (decoration != null && decoration.isCompleted()) {
            int fame = clan.getJunk() / decoration.getDetails().getValue();
            if (fame > 0) {
                ClanNotification notification = new ClanNotification(turnId, clan.getId());
                notification.setHeader(clan.getName());

                clan.changeFame(fame);
                notification.changeFame(fame);

                notificationService.addLocalizedTexts(notification.getText(), "research.decoration", new String[]{});
                notificationService.save(notification);
            }
        }
    }

    private void notifyAvailableResearch(Clan clan, ClanNotification notification) {
        for (Research research : clan.getResearch().values()) {
            if (research.getDetails().getInformationLevel() == clan.getInformationLevel()) {
                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "research.new", new String[]{},
                        research.getDetails().getName());
                notification.getDetails().add(detail);
            }
        }
    }

    private void markCharactersReady(Clan clan) {
        for (Character character : clan.getCharacters()) {
            character.setState(CharacterState.READY);
        }
    }

    private void handleHealing(Character character, ClanNotification notification) {
        int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();

        if (hitpointsMissing == 0) {
            return;
        }

        // each character that is ready heals
        int points = gameProperties.getHealing();
        if (character.getClan().getBuildings().containsKey(BuildingDetails.BuildingName.SICK_BAY)) {
            Building building = character.getClan().getBuildings().get(BuildingDetails.BuildingName.SICK_BAY);
            int bonus = building.getLevel() * gameProperties.getHealing();
            points += bonus;

            if (building.getLevel() > 0) {
                NotificationDetail healingBuildingDetail = new NotificationDetail();
                notificationService.addLocalizedTexts(healingBuildingDetail.getText(), "detail.healing.building",
                        new String[] {String.valueOf(bonus)});
                notification.getDetails().add(healingBuildingDetail);
            }
        }

        // lizard trait
        points += BonusUtils.getTraitBonus(character, BonusType.HEALING, notification, notificationService);

        int healing = Math.min(points, hitpointsMissing);
        character.changeHitpoints(healing);

        notificationService.addLocalizedTexts(notification.getText(), "character.healing", new String[] {});
        notification.changeHealing(healing);
    }

    private void handleRestingBonuses(Character character, ClanNotification notification) {
        Research cultivation = character.getClan().getResearch().get(ResearchName.CULTIVATION);
        if (cultivation != null && cultivation.isCompleted()) {
            int food = randomService.getRandomInt(1, RandomServiceImpl.K4);
            character.getClan().changeFood(food);

            notification.changeFood(food);
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.research.cultivation", new String[]{});
            notification.getDetails().add(detail);
        }
    }

    private void prepareStatistics(int turnId, Clan clan) {
        ClanTurnStatistics statistics = new ClanTurnStatistics(clan, turnId);
        statisticsRepository.save(statistics);
    }

}
