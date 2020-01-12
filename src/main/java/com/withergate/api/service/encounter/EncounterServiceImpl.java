package com.withergate.api.service.encounter;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.encounter.SolutionType;
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

        notificationService.addLocalizedTexts(notification.getText(), encounter.getDescriptionText(), new String[] {});

        boolean success = handleSolution(character, encounter.getType(), encounter.getDifficulty(), notification);

        if (success) {
            handleSuccess(encounter, character, notification);
        } else {
            handleFailure(encounter, character, notification);
        }

        return success;
    }

    @Override
    public boolean handleSolution(Character character, SolutionType type, int difficulty, ClanNotification notification) {
        boolean success = false;

        int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K6);
        switch (type) {
            case AUTOMATIC:
                success = true;
                break;
            case COMBAT:
                // handle combat and check if character won, if yes, handle reward
                success = combatService.handleSingleCombat(notification, difficulty, character);
                break;
            case INTELLECT:
                int totalIntellect = character.getIntellect() + diceRoll;
                log.debug("{} rolled dice and the total intellect value is {}", character.getName(), totalIntellect);
                if (totalIntellect >= difficulty) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(difficulty, diceRoll, totalIntellect));
                break;
            case SCAVENGE:
                int totalScavenge = character.getScavenge() + diceRoll;
                log.debug("{} rolled dice and the total scavenge value is {}", character.getName(), totalScavenge);
                if (totalScavenge >= difficulty) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(difficulty, diceRoll, totalScavenge));
                break;
            case CRAFTSMANSHIP:
                int totalCraftsmanship = character.getCraftsmanship() + diceRoll;
                log.debug("{} rolled dice and the total craftsmanship value is {}", character.getName(), totalCraftsmanship);
                if (totalCraftsmanship >= difficulty) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(difficulty, diceRoll, totalCraftsmanship));
                break;
            case COMBAT_ROLL:
                int totalCombat = character.getCombat() + diceRoll;
                log.debug("{} rolled dice and the total combat value is {}", character.getName(), totalCombat);
                if (totalCombat >= difficulty) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(difficulty, diceRoll, totalCombat));
                break;
            case INTELLECT_LOW:
                totalIntellect = character.getIntellect() + diceRoll;
                log.debug("{} rolled dice and the total intellect value is {}", character.getName(), totalIntellect);
                if (totalIntellect <= difficulty) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(difficulty, diceRoll, totalIntellect));
                break;
            case CRAFTSMANSHIP_LOW:
                totalCraftsmanship = character.getCraftsmanship() + diceRoll;
                log.debug("{} rolled dice and the total craftsmanship value is {}", character.getName(), totalCraftsmanship);
                if (totalCraftsmanship <= difficulty) {
                    success = true;
                }
                notification.getDetails().add(getActionRollDetail(difficulty, diceRoll, totalCraftsmanship));
                break;
            default:
                log.error("Unknown solution type triggered: {}!", type);
                break;
        }

        return success;
    }

    private void handleSuccess(Encounter encounter, Character character, ClanNotification notification) {
        log.debug("Computing reward for character {}", character.getId());

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), encounter.getSuccessText(), new String[] {});

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
        notificationService.addLocalizedTexts(notification.getText(), encounter.getFailureText(), new String[] {});

        Clan clan = character.getClan();

        switch (encounter.getPenalty()) {
            case NONE:
                break;
            case CAPS:
                // add caps
                int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K6) * 2; // random amount of caps
                int caps = Math.min(clan.getCaps(), diceRoll);

                clan.changeCaps(-caps);

                // update notification
                notification.changeCaps(-caps);
                break;
            case INJURY:
                int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);

                character.changeHitpoints(-injury);
                notification.changeInjury(injury);

                if (character.getHitpoints() < 1) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.character.injurydeath",
                            new String[] {character.getName()});
                    notification.getDetails().add(detail);
                    notification.setDeath(true);
                }
                break;
            default:
                log.error("Unknown type of penalty: {}!", encounter.getPenalty());
                break;
        }
    }

    private NotificationDetail getActionRollDetail(int difficulty, int roll, int result) {
        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.action.roll",
                new String[]{String.valueOf(difficulty), String.valueOf(roll), String.valueOf(result)});
        return detail;
    }

}
