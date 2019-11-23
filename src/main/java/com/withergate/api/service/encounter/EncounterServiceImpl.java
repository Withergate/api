package com.withergate.api.service.encounter;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.EncounterRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Encounter service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class EncounterServiceImpl implements EncounterService {

    private static final int BASE_REWARD = 3;

    private final EncounterRepository encounterRepository;
    private final ItemService itemService;
    private final RandomService randomService;
    private final CombatService combatService;
    private final NotificationService notificationService;

    @Override
    public boolean handleEncounter(ClanNotification notification, Character character, Location location) {
        // load random encounter from the repository
        List<Encounter> encounters = encounterRepository.findAllByLocation(location);
        int index = randomService.getRandomInt(0, encounters.size() - 1);
        Encounter encounter = encounters.get(index);

        log.debug("Processing {} with {} at {}", encounter.getType(), character.getName(), location.name());

        notificationService.addLocalizedTexts(notification.getText(), encounter.getDescriptionText(), new String[]{});

        boolean success = false;
        switch (encounter.getType()) {
            case COMBAT:
                // handle combat and check if character won, if yes, handle reward
                if (combatService.handleSingleCombat(notification, encounter.getDifficulty(), character)) {
                    handleSuccess(encounter, character, notification);
                    success = true;
                } else {
                    handleFailure(encounter, character, notification);
                }
                break;
            case INTELLECT:
                int totalIntellect = character.getIntellect() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total intellect value is {}", character.getName(), totalIntellect);
                if (totalIntellect < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                    success = true;
                }
                break;
            case SCAVENGE:
                int totalScavenge = character.getScavenge() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total scavenge value is {}", character.getName(), totalScavenge);
                if (totalScavenge < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                    success = true;
                }
                break;
            case CRAFTSMANSHIP:
                int totalCraftsmanship = character.getCraftsmanship() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total craftsmanship value is {}", character.getName(), totalCraftsmanship);
                if (totalCraftsmanship < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                    success = true;
                }
                break;
            case COMBAT_ROLL:
                int totalCombat = character.getCombat() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total combat value is {}", character.getName(), totalCombat);
                if (totalCombat < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                    success = true;
                }
                break;
            default:
                log.error("Unknown encounter type triggered: {}!", encounter.getType());
                break;
        }

        return success;
    }

    private void handleSuccess(Encounter encounter, Character character, ClanNotification notification) {
        log.debug("Computing reward for character {}", character.getId());

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), encounter.getSuccessText(), new String[]{});

        Clan clan = character.getClan();

        switch (encounter.getReward()) {
            case CAPS:
                // add caps
                int caps = randomService.getRandomInt(1, RandomServiceImpl.K6) + BASE_REWARD;

                clan.changeCaps(caps);
                notification.changeCaps(caps);
                break;
            case JUNK:
                // add junk
                int junk = randomService.getRandomInt(1, RandomServiceImpl.K6) + BASE_REWARD;
                clan.changeJunk(junk);
                notification.changeJunk(junk);
                break;
            case FOOD:
                // add food
                int food = randomService.getRandomInt(1, RandomServiceImpl.K6) + BASE_REWARD;
                clan.changeFood(food);
                notification.changeFood(food);
                break;
            case INFORMATION:
                // add information
                int information = randomService.getRandomInt(1, RandomServiceImpl.K6) + BASE_REWARD;
                clan.changeInformation(information);
                notification.changeInformation(information);
                break;
            case ITEM:
                // generate item
                itemService.generateItemForCharacter(character, notification);
                break;
            default:
                log.error("Unknown type of reward!");
                break;
        }
    }

    private void handleFailure(Encounter encounter, Character character, ClanNotification notification) {
        log.debug("Computing penalty for character {}", character.getId());

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), encounter.getFailureText(), new String[]{});

        // handle experience
        character.changeExperience(1);
        notification.changeExperience(1);

        Clan clan = character.getClan();

        switch (encounter.getPenalty()) {
            case NONE:
                break;
            case CAPS:
                // add caps
                int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K6) * 2; // random amount of caps
                int caps = Math.min(clan.getCaps(), diceRoll);

                clan.changeCaps(- caps);

                // update notification
                notification.changeCaps(- caps);
                break;
            case INJURY:
                int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);

                character.changeHitpoints(- injury);
                notification.changeInjury(injury);

                if (character.getHitpoints() < 1) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.character.injurydeath",
                            new String[]{character.getName()});
                    notification.getDetails().add(detail);
                    notification.setDeath(true);
                }
                break;
            default:
                log.error("Unknown type of penalty: {}!", encounter.getPenalty());
                break;
        }
    }

}
