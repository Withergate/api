package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.service.utils.BonusUtils;
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

    private static final int FAME_CAP = 5;

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final NotificationService notificationService;
    private final QuestService questService;
    private final BuildingService buildingService;
    private final TavernService tavernService;
    private final RandomService randomService;
    private final GameProperties gameProperties;

    @Override
    public void performClanTurnUpdates(Clan clan, int turnId) {
        log.debug("-> Performing turn updates for clan {}.", clan.getId());

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
        performResearchEndTurnUpdates(turnId, clan, ResearchBonusType.FOOD_FAME, clan.getFood());
        performResearchEndTurnUpdates(turnId, clan, ResearchBonusType.JUNK_FAME, clan.getJunk());

        // mark characters as ready
        markCharactersReady(clan);

        // probably redundant
        clanRepository.saveAndFlush(clan);
    }

    private void performFoodConsumption(int turnId, Clan clan) {
        log.debug("Food consumption for clan {}.", clan.getId());

        if (clan.getCharacters().isEmpty()) {
            return;
        }

        ClanNotification notification = new ClanNotification(turnId, clan.getId());
        notification.setHeader(clan.getName());
        notificationService.addLocalizedTexts(notification.getText(), "clan.foodConsumption", new String[] {});

        for (Character character : clan.getCharacters()) {
            int consumption = gameProperties.getFoodConsumption();

            // ascetic
            consumption -= BonusUtils.getBonus(character, BonusType.FOOD_CONSUMPTION, notification, notificationService);

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

                // update statistics
                clan.getStatistics().setStarvations(clan.getStatistics().getStarvations() + 1);

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
            notificationService.addLocalizedTexts(notification.getText(), "character.resting", new String[] {});

            // handle resting bonuses
            handleRestingBonuses(character, notification);

            handleHealing(character, notification);

            // save notification
            notificationService.save(notification);
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

    private void performResearchEndTurnUpdates(int turnId, Clan clan, ResearchBonusType bonusType, int resources) {
        log.debug("Performing end turn research updates");

        Research research = clan.getResearch(bonusType);
        if (research != null && research.isCompleted()) {
            int fame = resources / research.getDetails().getValue();
            if (fame > FAME_CAP) fame = FAME_CAP;
            if (fame > 0) {
                ClanNotification notification = new ClanNotification(turnId, clan.getId());
                notification.setHeader(clan.getName());

                clan.changeFame(fame);
                notification.changeFame(fame);

                notificationService.addLocalizedTexts(notification.getText(), research.getDetails().getBonusText(), new String[]{});

                // pay resources
                switch (bonusType) {
                    case FOOD_FAME:
                        clan.changeFood(- fame);
                        notification.changeFood(- fame);
                        break;
                    case JUNK_FAME:
                        clan.changeJunk(- fame);
                        notification.changeJunk(- fame);
                        break;
                    default:
                        log.error("Incompatible research type: {}", bonusType);
                }

                notificationService.save(notification);
            }
        }
    }

    private void notifyAvailableResearch(Clan clan, ClanNotification notification) {
        for (Research research : clan.getResearch()) {
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

        // trait
        points += BonusUtils.getBonus(character, BonusType.HEALING, notification, notificationService);

        int healing = Math.min(points, hitpointsMissing);
        character.changeHitpoints(healing);
        notification.changeHealing(healing);
    }

    private void handleRestingBonuses(Character character, ClanNotification notification) {
        int exp = BonusUtils.getBonus(character, BonusType.TRAINING, notification, notificationService);
        if (exp > 0) {
            notification.changeExperience(exp);
            character.changeExperience(exp);
        }

        Research research = character.getClan().getResearch(ResearchBonusType.REST_FOOD);
        if (research != null && research.isCompleted()) {
            int food = randomService.getRandomInt(1, RandomServiceImpl.K4);
            character.getClan().changeFood(food);

            notification.changeFood(food);
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), research.getDetails().getBonusText(), new String[]{});
            notification.getDetails().add(detail);
        }
    }

}
