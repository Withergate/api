package com.withergate.api.service.encounter;

import com.withergate.api.GameProperties;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.combat.CombatResult;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.service.IRandomService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.ICharacterService;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final GameProperties gameProperties;

    public CombatService(IRandomService randomService, ICharacterService characterService,
                         GameProperties gameProperties) {
        this.randomService = randomService;
        this.characterService = characterService;
        this.gameProperties = gameProperties;
    }

    @Override
    public boolean handleEncounterCombat(ClanNotification notification, Encounter encounter, Character character,
                                         Location location) {
        log.debug("Handling encounter combat for character {} and encounter difficulty {}.", character.getName(),
                encounter.getDifficulty());

        // prepare enemy
        Character enemy = new Character();
        enemy.setId(-1);
        enemy.setName("Enemy");
        enemy.setCombat(encounter.getDifficulty());
        enemy.setHitpoints(encounter.getDifficulty() + randomService.getRandomInt(1, RandomService.K6));
        enemy.setMaxHitpoints(enemy.getHitpoints());

        CombatResult result = handleCombat(character, notification, enemy, null);

        // compute combat result
        if (result.getLoser().getId() == character.getId()) {
            // handle experience
            character.setExperience(character.getExperience() + 1);
            notification.setExperience(1);

            // check if character is still alive
            if (character.getHitpoints() < 1) {
                notification.setResult("Unfortunately, [" + character.getName() + "] died during the combat.");
            } else {
                notification.setResult(encounter.getFailureText(character, location));
            }
        } else {
            // combat won - update notification
            notification.setResult(encounter.getSuccessText(character, location));

            // handle experience
            character.setExperience(character.getExperience() + 2);
            notification.setExperience(2);

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

            // process fight
            ClanNotification winnerNotification = new ClanNotification();
            ClanNotification loserNotification = new ClanNotification();
            CombatResult result = handleCombat(character1, winnerNotification, character2, loserNotification);

            // create results
            ArenaResult winnerResult = getWinnerResult(result.getWinner(), result.getLoser(), winnerNotification);
            if (winnerResult != null) results.add(winnerResult);
            ArenaResult loserResult = getLoserResult(result.getLoser(), result.getWinner(), loserNotification);
            if (loserResult != null) results.add(loserResult);
        }

        return results;
    }

    private ArenaResult getWinnerResult(Character character, Character opponent, ClanNotification notification) {
        log.debug("{} won the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) {
            log.debug("Non-player character.");
            return null;
        }

        notification.setClanId(character.getClan().getId());
        notification.setText("[" + character.getName() + "] faced [" + opponent.getName() + "] in the arena.");

        notification.setResult("[" + character.getName() + "] won the fight!");

        character.getClan()
                .setCaps(character.getClan().getCaps() + gameProperties.getArenaCaps()); // add caps to the winner
        character.getClan()
                .setFame(character.getClan().getFame() + gameProperties.getArenaFame()); // add fame to the winner

        notification.setCapsIncome(gameProperties.getArenaCaps());
        notification.setFameIncome(gameProperties.getArenaFame());

        // handle experience
        character.setExperience(character.getExperience() + 2);
        notification.setExperience(2);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setNotification(notification);

        return result;
    }

    private ArenaResult getLoserResult(Character character, Character opponent, ClanNotification notification) {
        log.debug("{} lost the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) {
            log.debug("Non-player character.");
            return null;
        }

        notification.setClanId(character.getClan().getId());
        notification.setText("[" + character.getName() + "] faced [" + opponent.getName() + "] in the arena.");

        notification.setResult("[" + character.getName() + "] lost the fight!");

        // handle experience
        character.setExperience(character.getExperience() + 1);
        notification.setExperience(1);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setNotification(notification);

        return result;
    }

    private CombatResult handleCombat(Character character1, ClanNotification notification1, Character character2,
                                      ClanNotification notification2) {
        while (true) {
            CombatResult result = handleCombatRound(character1, character2);
            log.debug("Combat round result: {}", result);

            if (notification1 != null) {
                notification1.getDetails().addAll(result.getDetails());
            }
            if (notification2 != null) {
                notification2.getDetails().addAll(result.getDetails());
            }

            if (result.isFinished()) {
                return result;
            }
        }
    }

    private CombatResult handleCombatRound(Character character1, Character character2) {
        CombatResult result = new CombatResult();
        List<NotificationDetail> details = new ArrayList<>();
        boolean finished = false;

        // initial dice rolls
        int roll1 = randomService.getRandomInt(1, RandomService.K6);
        int roll2 = randomService.getRandomInt(1, RandomService.K6);
        NotificationDetail detailRoll = new NotificationDetail();
        detailRoll.setText(
                "[" + character1.getName() + "] rolled " + roll1 + " on combat dice. [" + character2.getName()
                        + "] rolled " + roll2 + ".");
        details.add(detailRoll);

        // compare combat values
        int combat1 = character1.getTotalCombat() + roll1;
        int combat2 = character2.getTotalCombat() + roll2;

        // handle draw
        if (combat1 == combat2) {
            // one of the characters gets random bonus
            if (randomService.getRandomInt(1, RandomService.K100) <= 50) {
                combat1++;
            } else {
                combat2++;
            }
        }

        Character winner;
        Character loser;
        int combatWinner;
        int combatLoser;
        int injury;
        if (combat1 > combat2) {
            winner = character1;
            loser = character2;
            combatWinner = combat1;
            combatLoser = combat2;
        } else {
            winner = character2;
            loser = character1;
            combatWinner = combat2;
            combatLoser = combat1;
        }
        injury = combatWinner - combatLoser;
        loser.setHitpoints(loser.getHitpoints() - injury);
        NotificationDetail detailCombat = new NotificationDetail();
        detailCombat
                .setText("[" + winner.getName() + "] won the round with combat value " + combatWinner + " and dealt "
                        + injury + " damage to [" + loser.getName() + "].");
        details.add(detailCombat);

        if (loser.getHitpoints() < 1) {
            finished = true;
            NotificationDetail detailDeath = new NotificationDetail();
            detailDeath.setText("[" + loser.getName() + "] died.");
            details.add(detailDeath);
        }

        // compute the chance to flee the combat
        int fleeRoll = randomService.getRandomInt(1, RandomService.K100);
        double fleeChance = 100 - ((double) loser.getHitpoints() / loser.getMaxHitpoints()) * 100;
        if (loser.getHitpoints() > 0 && fleeChance > fleeRoll) {
            finished = true;
            NotificationDetail fleeDetail = new NotificationDetail();
            fleeDetail.setText(
                    "[" + loser.getName() + "] fleed the combat with " + (int) fleeChance + " chance and " + fleeRoll
                            + " dice roll.");
            details.add(fleeDetail);
        }

        result.setFinished(finished);
        result.setWinner(winner);
        result.setLoser(loser);
        result.setDetails(details);

        return result;
    }
}
