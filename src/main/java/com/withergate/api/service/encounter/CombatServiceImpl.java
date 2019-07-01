package com.withergate.api.service.encounter;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.combat.CombatResult;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.item.WeaponType;
import com.withergate.api.model.location.ArenaResult;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Combat service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class CombatServiceImpl implements CombatService {

    private static final int ARENA_CAPS = 20;
    private static final int ARENA_FAME = 10;

    private final RandomService randomService;
    private final CharacterService characterService;
    private final NotificationService notificationService;

    @Override
    public boolean handleSingleCombat(ClanNotification notification, int difficulty, Character character) {
        log.debug("Handling encounter combat for character {} and encounter difficulty {}.", character.getName(),
                difficulty);

        // prepare enemy
        Character enemy = new Character();
        enemy.setId(-1);
        enemy.setName("Enemy");
        enemy.setCombat(difficulty);
        enemy.setHitpoints(difficulty + randomService.getRandomInt(1, RandomServiceImpl.K10));
        enemy.setMaxHitpoints(enemy.getHitpoints());

        CombatResult result = handleCombat(character, notification, enemy, new ClanNotification());

        // compute combat result
        return result.getWinner().getId() == character.getId();
    }

    @Override
    public List<ArenaResult> handleArenaFights(List<Character> characters) {
        List<ArenaResult> results = new ArrayList<>(characters.size());

        // check if list is odd
        if (characters.size() % 2 != 0) {
            log.debug("There is odd number of arena competitors. Generating random opponent...");
            characters.add(characterService.generateRandomCharacter(new CharacterFilter()));
        }

        // split characters into fighting pairs
        for (int i = 0; i < characters.size(); i += 2) {
            Character character1 = characters.get(i);
            Character character2 = characters.get(i + 1);

            // process fight
            ClanNotification notification1 = new ClanNotification();
            ClanNotification notification2 = new ClanNotification();
            CombatResult result = handleCombat(character1, notification1, character2, notification2);

            ClanNotification winnerNotification;
            ClanNotification loserNotification;
            if (result.getWinner().getId() == character1.getId()) {
                winnerNotification = notification1;
                loserNotification = notification2;
            } else {
                winnerNotification = notification2;
                loserNotification = notification1;
            }

            // create results
            ArenaResult winnerResult = getWinnerResult(result.getWinner(), result.getLoser(), winnerNotification);
            if (winnerResult != null) {
                results.add(winnerResult);
            }
            ArenaResult loserResult = getLoserResult(result.getLoser(), result.getWinner(), loserNotification);
            if (loserResult != null) {
                results.add(loserResult);
            }
        }

        return results;
    }

    private CombatResult handleCombat(Character character1, ClanNotification notification1, Character character2,
                                      ClanNotification notification2) {
        while (true) {
            CombatResult result = handleCombatRound(character1, notification1, character2, notification2);
            log.debug("Combat round result: {}", result);

            // add details to both players
            for (NotificationDetail detail : result.getDetails()) {
                notification1.getDetails().add(new NotificationDetail(detail));
                notification2.getDetails().add(new NotificationDetail(detail));
            }

            if (result.isFinished()) {
                return result;
            }
        }
    }

    private CombatResult handleCombatRound(Character character1, ClanNotification notification1, Character character2,
                                           ClanNotification notification2) {
        List<NotificationDetail> details = new ArrayList<>();
        boolean finished = false;

        // initial dice rolls
        int roll1 = randomService.getRandomInt(1, RandomServiceImpl.K6);
        int roll2 = randomService.getRandomInt(1, RandomServiceImpl.K6);
        NotificationDetail detailRoll = new NotificationDetail();
        notificationService.addLocalizedTexts(detailRoll.getText(), "detail.combat.rolls",
                new String[]{character1.getName(), String.valueOf(roll1), character2.getName(), String.valueOf(roll2)});
        details.add(detailRoll);

        // compare combat values
        int combat1 = character1.getTotalCombat() + roll1 + getCombatBonus(character1, notification1);
        int combat2 = character2.getTotalCombat() + roll2 + getCombatBonus(character2, notification2);

        // handle draw
        if (combat1 == combat2) {
            // one of the characters gets random bonus
            if (randomService.getRandomInt(1, RandomServiceImpl.K100) <= 50) {
                combat1++;
            } else {
                combat2++;
            }
        }

        // select winner and loser
        Character winner;
        Character loser;
        int combatWinner;
        int combatLoser;
        int injury;
        ClanNotification winnerNotification;
        ClanNotification loserNotification;
        if (combat1 > combat2) {
            winner = character1;
            loser = character2;
            combatWinner = combat1;
            combatLoser = combat2;
            winnerNotification = notification1;
            loserNotification = notification2;
        } else {
            winner = character2;
            loser = character1;
            combatWinner = combat2;
            combatLoser = combat1;
            winnerNotification = notification2;
            loserNotification = notification1;
        }

        // compute injury
        injury = combatWinner - combatLoser - getArmor(loser);
        if (injury < 1) injury = 1;
        loser.setHitpoints(loser.getHitpoints() - injury);
        loserNotification.setInjury(loserNotification.getInjury() + injury);

        // update notification
        NotificationDetail detailCombat = new NotificationDetail();
        notificationService.addLocalizedTexts(detailCombat.getText(), "detail.combat.roundresult",
                new String[]{winner.getName(), String.valueOf(combatWinner), loser.getName(),
                        String.valueOf(combatLoser), loser.getName(), String.valueOf(injury)});
        details.add(detailCombat);

        // check death
        if (loser.getHitpoints() < 1) {
            finished = true;
            NotificationDetail detailDeath = new NotificationDetail();
            notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.injurydeath",
                    new String[]{loser.getName()});
            details.add(detailDeath);
        }

        // compute the chance to flee the combat
        int fleeRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        double fleeChance = 100 - ((double) loser.getHitpoints() / loser.getMaxHitpoints()) * 100;
        if (loser.getHitpoints() > 0 && fleeChance > fleeRoll) {
            finished = true;
            NotificationDetail fleeDetail = new NotificationDetail();
            notificationService.addLocalizedTexts(fleeDetail.getText(), "detail.combat.flee",
                    new String[]{loser.getName(), String.valueOf(fleeRoll), String.valueOf((int) fleeChance)});

            details.add(fleeDetail);
        }

        // create result
        CombatResult result = new CombatResult();
        result.setFinished(finished);
        result.setWinner(winner);
        result.setLoser(loser);
        result.setDetails(details);

        return result;
    }

    private ArenaResult getWinnerResult(Character character, Character opponent, ClanNotification notification) {
        log.debug("{} won the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) {
            log.debug("Non-player character.");
            return null;
        }

        notification.setClanId(character.getClan().getId());
        notificationService.addLocalizedTexts(notification.getText(), "combat.arena.description",
                new String[]{character.getName(), opponent.getName()});
        notificationService
                .addLocalizedTexts(notification.getText(), "combat.arena.win", new String[]{character.getName()});

        character.getClan()
                .setCaps(character.getClan().getCaps() + ARENA_CAPS); // add caps to the winner
        character.getClan()
                .setFame(character.getClan().getFame() + ARENA_FAME); // add fame to the winner

        notification.setCapsIncome(ARENA_CAPS);
        notification.setFameIncome(ARENA_FAME);

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
        notificationService.addLocalizedTexts(notification.getText(), "combat.arena.description",
                new String[]{character.getName(), opponent.getName()});
        notificationService
                .addLocalizedTexts(notification.getText(), "combat.arena.lose", new String[]{character.getName()});

        // handle experience
        character.setExperience(character.getExperience() + 1);
        notification.setExperience(1);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setNotification(notification);

        return result;
    }

    // add combat bonus to a character with certain traits if conditions are met
    private int getCombatBonus(Character character, ClanNotification notification) {
        if (character.getTraits().containsKey(TraitDetails.TraitName.FIGHTER) && character.getWeapon() != null
                && character.getWeapon().getDetails().getType() == WeaponType.MELEE) {
            if (randomService.getRandomInt(1, 100) < 50) {
                NotificationDetail detail = new NotificationDetail();
                notificationService
                        .addLocalizedTexts(detail.getText(), "detail.trait.fighter", new String[]{character.getName()});
                notification.getDetails().add(detail);

                return character.getTraits().get(TraitName.FIGHTER).getDetails().getBonus();
            }
        }
        return 0;
    }

    private int getArmor(Character character) {
        if (character.getOutfit() != null) {
            return character.getOutfit().getDetails().getArmor();
        }

        return 0;
    }

}
