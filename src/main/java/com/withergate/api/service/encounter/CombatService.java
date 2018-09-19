package com.withergate.api.service.encounter;

import com.withergate.api.Constants;
import com.withergate.api.model.ArenaResult;
import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.service.IRandomService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.ICharacterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Combat service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class CombatService implements ICombatService {

    private final IRandomService randomService;
    private final ICharacterService characterService;

    /**
     * Constructor.
     *
     * @param randomService random service
     * @param characterService character service
     */
    public CombatService(IRandomService randomService, ICharacterService characterService) {
        this.randomService = randomService;
        this.characterService = characterService;
    }

    @Override
    public boolean handleCombat(ClanNotification notification, Encounter encounter, Character character, Location location) {
        int diceRoll = randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);
        int totalCombat = character.getTotalCombat() + diceRoll;
        log.debug("{} rolled dice and the total combat value is {}", character.getName(), totalCombat);

        // compute combat result
        if (totalCombat < encounter.getDifficulty()) {
            // combat lost - compute injury
            int injury = computeInjury();
            character.setHitpoints(character.getHitpoints() - injury);

            // update notification details
            notification.setDetails("Dice roll: [" + diceRoll + "], total combat: [" + totalCombat +
                    "], enemy combat: [" + encounter.getDifficulty() + "], injury: [" + injury +"] hitpoints lost.");

            // check if character is still alive
            if (character.getHitpoints() < 1) {
                log.debug("{} died during the combat!", character.getName());

                notification.setResult("Unfortunately, [" + character.getName() + "] died during the combat.");
            } else {
                log.debug("Character {} lost {} hitpoints and now has {} hitpoints.", character.getName(), injury, character.getHitpoints());
                notification.setResult(encounter.getFailureText(character, location));
            }
        } else {
            // combat won - update notification
            notification.setResult(encounter.getSuccessText(character, location));

            notification.setDetails("Dice roll: [" + diceRoll + "], total combat: [" + totalCombat +
                    "], enemy combat: [ " + encounter.getDifficulty() + "].");

            return true;
        }

        return false;
    }

    @Override
    public List<ArenaResult> handleArenaFights(List<Character> characters) {
        List<ArenaResult> results = new ArrayList<>(characters.size());

        // check if list is odd
        if (characters.size() % 2 != 0) {
            log.debug("There is odd number of arena competitors. Generating random opponent...");
            characters.add(characterService.generateRandomCharacter());
        }

        // split characters into fighting pairs
        for (int i = 0; i < characters.size(); i += 2) {
            Character character1 = characters.get(i);
            Character character2 = characters.get(i + 1);

            handleFight(character1, character2, results);
        }

        return results;
    }

    private void handleFight(Character character1, Character character2, List<ArenaResult> results) {
        int combat1 = character1.getTotalCombat() + randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);
        int combat2 = character2.getTotalCombat() + randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);

        log.debug("{} has total combat of {}", character1.getName(), combat1);
        log.debug("{} has total combat of {}", character2.getName(), combat2);

        // handle draw
        if (combat1 == combat2) {
            // one of the characters gets random bonus
            if (randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE) <= 50) {
                combat1++;
            } else {
                combat2++;
            }
        }

        // handle fight result
        Character winner;
        Character loser;
        if (combat1 > combat2) {
            // character1 won
            winner = character1;
            loser = character2;
        } else {
            // character2 won
            winner = character2;
            loser = character1;
        }

        // add results
        ArenaResult result1 = getWinnerResult(winner, loser);
        if (result1 != null) results.add(result1);
        ArenaResult result2 = getLoserResult(loser, winner);
        if (result2 != null) results.add(result2);
    }

    private ArenaResult getWinnerResult(Character character, Character opponent) {
        log.debug("{} won the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) {
            log.debug("Non-player character.");
            return null;
        }

        ClanNotification notification = new ClanNotification();
        notification.setClanId(character.getClan().getId());
        notification.setText("[" + character.getName() + "] faced [" + opponent.getName() + "] in the arena.");

        notification.setResult("[" + character.getName() + "] won the fight!");

        character.getClan().setCaps(character.getClan().getCaps() + Constants.ARENA_CAPS); // add caps to the winner
        character.getClan().setFame(character.getClan().getFame() + Constants.ARENA_FAME); // add fame to the winner

        notification.setCapsIncome(Constants.ARENA_CAPS);
        notification.setFameIncome(Constants.ARENA_FAME);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setNotification(notification);

        return result;
    }

    private ArenaResult getLoserResult(Character character, Character opponent) {
        log.debug("{} lost the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) {
            log.debug("Non-player character.");
            return null;
        }

        ClanNotification notification = new ClanNotification();
        notification.setClanId(character.getClan().getId());
        notification.setText("[" + character.getName() + "] faced [" + opponent.getName() + "] in the arena.");

        notification.setResult("[" + character.getName() + "] lost the fight!");

        // make the injury non-fatal
        int injury = Math.min(computeInjury(), character.getHitpoints() - 1);
        character.setHitpoints(character.getHitpoints() - injury);

        notification.setInjury(injury);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setNotification(notification);

        return result;
    }

    private int computeInjury() {
        // compute basic injury
        int injury = randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);

        // chance for critical hit
        if (randomService.getRandomInt(1, RandomService.PERCENTAGE_DICE) < 20) {
            log.debug("Critical hit triggered!");
            injury *= 2;
        }
        return injury;
    }
}
