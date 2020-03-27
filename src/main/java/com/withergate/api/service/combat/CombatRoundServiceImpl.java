package com.withergate.api.service.combat;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.combat.CombatResult;
import com.withergate.api.game.model.item.ItemDetails.WeaponType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationCombatRound;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.profile.AchievementService;
import com.withergate.api.service.utils.BonusUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Combat round service implementation. Used for detail combat mechanics.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CombatRoundServiceImpl implements CombatRoundService {

    private final RandomService randomService;
    private final NotificationService notificationService;
    private AchievementService achievementService;

    @Autowired
    public void setAchievementService(@Lazy AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @Override
    public CombatResult handleCombatRound(
            Character character1, ClanNotification notification1, Character character2,
            ClanNotification notification2, int round) {
        List<NotificationDetail> details = new ArrayList<>();
        boolean finished = false;

        NotificationCombatRound notificationCombat = new NotificationCombatRound();
        notificationCombat.setRound(round);
        notificationCombat.setName1(character1.getName());
        notificationCombat.setName2(character2.getName());
        notificationCombat.setHealth1(character1.getHitpoints());
        notificationCombat.setHealth2(character2.getHitpoints());
        notificationCombat.setCombat1(character1.getTotalCombat());
        notificationCombat.setCombat2(character2.getTotalCombat());

        // initial dice rolls
        int roll1 = randomService.getRandomInt(1, RandomServiceImpl.K6);
        notificationCombat.setRoll1(roll1);
        int roll2 = randomService.getRandomInt(1, RandomServiceImpl.K6);
        notificationCombat.setRoll2(roll2);

        // compare combat values
        int combat1 = character1.getTotalCombat() + roll1 + getCombatBonus(character1, notification1);
        int combat2 = character2.getTotalCombat() + roll2 + getCombatBonus(character2, notification2);

        // handle draw
        if (combat1 == combat2) {
            NotificationDetail detailDraw = new NotificationDetail();
            // one of the characters gets random bonus
            if (randomService.getRandomInt(1, RandomServiceImpl.K100) <= 50) {
                notificationService.addLocalizedTexts(detailDraw.getText(), "detail.combat.draw", new String[]{character1.getName()});
                combat1++;
            } else {
                notificationService.addLocalizedTexts(detailDraw.getText(), "detail.combat.draw", new String[]{character2.getName()});
                combat2++;
            }
            details.add(detailDraw);
        }

        // select winner and loser
        RoundOutcome outcome = new RoundOutcome(character1, character2, combat1, combat2, notification1, notification2);

        // compute injury
        int armor = getArmor(outcome.getLoser(), outcome.getLoserNotification(), outcome.getWinner(), outcome.getWinnerNotification());
        notificationCombat.setArmor(armor);
        int injury = outcome.getCombatWinner() - outcome.getCombatLoser() - armor;
        if (injury < 1) injury = 1;
        outcome.getLoser().changeHitpoints(- injury);
        outcome.getLoserNotification().changeInjury(injury);
        notificationCombat.setInjury(injury);
        notificationCombat.setLoser(outcome.getLoser().getName());

        // save combat results
        NotificationDetail detailSummary = new NotificationDetail();
        notificationService.addLocalizedTexts(detailSummary.getText(), "detail.combat.summary", new String[]{String.valueOf(round)});
        detailSummary.setCombatRound(notificationCombat);
        details.add(detailSummary);

        // check death
        if (outcome.getLoser().getHitpoints() < 1) {
            finished = true;
            NotificationDetail detailDeath = new NotificationDetail();
            notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.injurydeath",
                    new String[]{outcome.getLoser().getName()});
            details.add(detailDeath);
            if (!outcome.getLoser().isNpc()) {
                outcome.getLoserNotification().setDeath(true);

                // award achievements
                handleDeathAchievements(outcome);
            }
        }

        // compute the chance to flee the combat
        int fleeRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        double fleeChance = 100 - ((double) outcome.getLoser().getHitpoints() / outcome.getLoser().getMaxHitpoints()) * 100;
        if (outcome.getLoser().getHitpoints() > 0
                && (outcome.getLoser().getHitpoints() < outcome.getLoser().getMaxHitpoints() / 3.0 || fleeChance > fleeRoll)) {
            finished = true;
            NotificationDetail fleeDetail = new NotificationDetail();
            notificationService.addLocalizedTexts(fleeDetail.getText(), "detail.combat.flee",
                    new String[]{outcome.getLoser().getName(), String.valueOf((int) fleeChance)});

            details.add(fleeDetail);

            // award achievements
            handleFleeAchievements(outcome.getLoser());
            handleVictoryAchievements(outcome.getWinner());
        }

        // create result
        CombatResult result = new CombatResult();
        result.setFinished(finished);
        result.setWinner(outcome.getWinner());
        result.setLoser(outcome.getLoser());
        result.setDetails(details);

        return result;
    }

    // add combat bonus to a character with certain traits if conditions are met
    private int getCombatBonus(Character character, ClanNotification notification) {
        int bonus = 0;

        if (character.getWeapon() != null && character.getWeapon().getDetails().getWeaponType().equals(WeaponType.MELEE)) {
            bonus += BonusUtils.getBonus(character, BonusType.COMBAT_MELEE, notification, notificationService);
        }

        if (character.getWeapon() != null && character.getWeapon().getDetails().getWeaponType().equals(WeaponType.RANGED)) {
            bonus += BonusUtils.getBonus(character, BonusType.COMBAT_RANGED, notification, notificationService);
        }

        if (character.getWeapon() == null && character.getOutfit() == null) {
            bonus += BonusUtils.getBonus(character, BonusType.COMBAT_UNARMED, notification, notificationService);
        }

        return bonus;
    }

    private int getArmor(Character defender, ClanNotification defenderNotification,
                         Character attacker, ClanNotification attackerNotification) {
        int armor = 0;

        if (defender.getOutfit() != null) {
            armor += defender.getOutfit().getDetails().getCombat();
        }

        if (armor > 0) {
            armor -= BonusUtils.getBonus(attacker, BonusType.PIERCING, attackerNotification, notificationService);
            // update notification for defender as well
            BonusUtils.getBonus(attacker, BonusType.PIERCING, defenderNotification, notificationService);
            if (armor < 0) {
                armor = 0;
            }
        }

        return armor;
    }

    private void handleVictoryAchievements(Character character) {
        if (!character.isNpc() && character.getHitpoints() == 1) {
            achievementService.checkAchievementAward(character.getClan().getId(), AchievementType.COMBAT_WIN_LUCKY);
        }
    }

    private void handleDeathAchievements(RoundOutcome outcome) {
        achievementService.checkAchievementAward(outcome.getLoser().getClan().getId(), AchievementType.COMBAT_DEATH);
        if (!outcome.getWinner().isNpc()) {
            achievementService.checkAchievementAward(outcome.getWinner().getClan().getId(), AchievementType.COMBAT_KILL);
        }
    }

    private void handleFleeAchievements(Character character) {
        if (!character.isNpc() && character.getHitpoints() == 1) {
            achievementService.checkAchievementAward(character.getClan().getId(), AchievementType.COMBAT_FLEE_LUCKY);
        }
    }

    @Getter
    private static class RoundOutcome {
        private final Character winner;
        private final Character loser;
        private final int combatWinner;
        private final int combatLoser;
        private final ClanNotification loserNotification;
        private final ClanNotification winnerNotification;

        RoundOutcome(Character character1, Character character2, int combat1, int combat2,
                            ClanNotification notification1, ClanNotification notification2) {
            if (combat1 > combat2) {
                winner = character1;
                loser = character2;
                combatWinner = combat1;
                combatLoser = combat2;
                loserNotification = notification2;
                winnerNotification = notification1;
            } else {
                winner = character2;
                loser = character1;
                combatWinner = combat2;
                combatLoser = combat1;
                loserNotification = notification1;
                winnerNotification = notification2;
            }
        }
    }
}
