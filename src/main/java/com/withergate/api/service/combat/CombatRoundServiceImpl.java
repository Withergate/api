package com.withergate.api.service.combat;

import com.withergate.api.game.model.BonusType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.combat.CombatResult;
import com.withergate.api.game.model.item.ItemDetails.WeaponType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationCombatRound;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.service.BonusUtils;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Combat round service implementation. Used for detail combat mechanics.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class CombatRoundServiceImpl implements CombatRoundService {

    private final RandomService randomService;
    private final NotificationService notificationService;

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
        notificationCombat.setCombat1(combat1);
        notificationCombat.setCombat2(combat2);

        // select winner and loser
        RoundOutcome outcome = new RoundOutcome(character1, character2, combat1, combat2, notification1, notification2);

        // compute injury
        int armor = getArmor(outcome.getLoser());
        notificationCombat.setArmor(armor);
        int injury = outcome.getCombatWinner() - outcome.getCombatLoser() - armor;
        if (injury < 1) injury = 1;
        outcome.getLoser().changeHitpoints(- injury);
        outcome.getLoserNotification().changeInjury(injury);
        notificationCombat.setInjury(injury);
        notificationCombat.setLoser(outcome.getLoser().getName());

        // check death
        if (outcome.getLoser().getHitpoints() < 1) {
            finished = true;
            NotificationDetail detailDeath = new NotificationDetail();
            notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.injurydeath",
                    new String[]{outcome.getLoser().getName()});
            details.add(detailDeath);
            notification1.setDeath(true);
            notification2.setDeath(true);
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
        }

        // create result
        CombatResult result = new CombatResult();
        result.setFinished(finished);
        result.setWinner(outcome.getWinner());
        result.setLoser(outcome.getLoser());
        result.setDetails(details);

        // save combat results
        NotificationDetail detailSummary = new NotificationDetail();
        notificationService.addLocalizedTexts(detailSummary.getText(), "detail.combat.summary", new String[]{String.valueOf(round)});
        detailSummary.setCombatRound(notificationCombat);
        details.add(detailSummary);

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

    private int getArmor(Character character) {
        if (character.getOutfit() != null) {
            return character.getOutfit().getDetails().getCombat();
        }

        return 0;
    }

    @Getter
    private static class RoundOutcome {
        private final Character winner;
        private final Character loser;
        private final int combatWinner;
        private final int combatLoser;
        private final ClanNotification loserNotification;

        RoundOutcome(Character character1, Character character2, int combat1, int combat2,
                            ClanNotification notification1, ClanNotification notification2) {
            if (combat1 > combat2) {
                winner = character1;
                loser = character2;
                combatWinner = combat1;
                combatLoser = combat2;
                loserNotification = notification2;
            } else {
                winner = character2;
                loser = character1;
                combatWinner = combat2;
                combatLoser = combat1;
                loserNotification = notification1;
            }
        }
    }
}
