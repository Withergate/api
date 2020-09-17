package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.utils.BonusUtils;
import com.withergate.api.service.utils.ResourceUtils;
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

    private static final int FAME_CAP = 6;

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final NotificationService notificationService;
    private final QuestService questService;
    private final BuildingService buildingService;
    private final TavernService tavernService;
    private final GameProperties gameProperties;

    @Override
    public void performClanTurnUpdates(Clan clan, int turnId) {
        log.debug("-> Performing turn updates for clan {}.", clan.getId());

        // passive buildings
        buildingService.processPassiveBuildingBonuses(turnId, clan);

        // tavern offers
        tavernService.prepareTavernOffers(clan);

        // perform character leveling
        performCharacterLeveling(turnId, clan);

        // food consumption
        performFoodConsumption(turnId, clan);

        // check information level
        checkInformationLevel(turnId, clan);

        // perform end-turn research updates
        performResearchEndTurnUpdates(turnId, clan, ResearchBonusType.FOOD_FAME, clan.getFood());
        performResearchEndTurnUpdates(turnId, clan, ResearchBonusType.JUNK_FAME, clan.getJunk());
        performEndTurnFactionResearchBonuses(turnId, clan);

        // pay loan
        payLoan(clan, turnId);

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
                ResourceUtils.changeFood(- consumption, clan, notification);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.foodConsumption",
                        new String[] {character.getName()});
                notification.getDetails().add(detail);
            } else {
                log.debug("Character {} is starving,", character.getName());

                character.changeHitpoints(- gameProperties.getStarvationInjury());

                // update statistics
                clan.getStatistics().setStarvations(clan.getStatistics().getStarvations() + 1);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.starving", new String[] {character.getName()});
                notification.getDetails().add(detail);
                ResourceUtils.changeFame(- gameProperties.getStarvationFame(),"STARVATION", clan, notification);
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

    private void performCharacterLeveling(int turnId, Clan clan) {
        for (Character character : clan.getCharacters()) {
            if (character.getExperience() >= character.getNextLevelExperience() && character.getHitpoints() > 0) {
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

            ResourceUtils.changeInformation(- clan.getNextLevelInformation(), clan, null);
            clan.setInformationLevel(clan.getInformationLevel() + 1);

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notificationService.addLocalizedTexts(notification.getText(), "clan.information.levelup", new String[] {});

            // assign quests
            questService.assignQuests(clan, notification, turnId);

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

                ResourceUtils.changeFame(fame, research.getDetails().getIdentifier(), clan, notification);

                notificationService.addLocalizedTexts(notification.getText(), research.getDetails().getBonusText(), new String[]{});

                // pay resources
                switch (bonusType) {
                    case FOOD_FAME:
                        ResourceUtils.changeFood(- fame, clan, notification);
                        break;
                    case JUNK_FAME:
                        ResourceUtils.changeJunk(- fame, clan, notification);
                        break;
                    default:
                        log.error("Incompatible research type: {}", bonusType);
                }

                notificationService.save(notification);
            }
        }
    }

    private void performEndTurnFactionResearchBonuses(int turnId, Clan clan) {
        Research research = clan.getResearch(ResearchBonusType.NO_FACTION_FAME);
        if (research != null && research.isCompleted() && clan.getFaction() == null) {
            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notification.setImageUrl(research.getDetails().getImageUrl());

            ResourceUtils.changeFame(research.getDetails().getValue(), research.getDetails().getIdentifier(), clan, notification);

            notificationService.addLocalizedTexts(notification.getText(), research.getDetails().getBonusText(), new String[]{});
;           notificationService.save(notification);
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

    private void payLoan(Clan clan, int turn) {
        if (clan.isActiveLoan() && clan.getCaps() >= gameProperties.getLoanPayback()) {
            // pay interest
            ClanNotification notification = new ClanNotification(turn, clan.getId());
            notification.setHeader(clan.getName());

            notificationService.addLocalizedTexts(notification.getText(), "loan.interest", new String[]{});
            ResourceUtils.changeCaps(- gameProperties.getLoanPayback(), clan, notification);

            notificationService.save(notification);
        }
    }

}
