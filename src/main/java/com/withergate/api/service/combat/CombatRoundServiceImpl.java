package com.withergate.api.service.combat;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.combat.CombatResult;
import com.withergate.api.model.item.WeaponType;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        RoundOutcome outcome = new RoundOutcome(character1, character2, combat1, combat2, notification1, notification2);

        // compute injury
        int injury = outcome.getCombatWinner() - outcome.getCombatLoser() - getArmor(outcome.getLoser());
        if (injury < 1) injury = 1;
        outcome.getLoser().setHitpoints(outcome.getLoser().getHitpoints() - injury);
        outcome.getLoserNotification().setInjury(outcome.getLoserNotification().getInjury() + injury);

        // update notification
        NotificationDetail detailCombat = new NotificationDetail();
        notificationService.addLocalizedTexts(detailCombat.getText(), "detail.combat.roundresult",
                new String[]{outcome.getWinner().getName(), String.valueOf(outcome.getCombatWinner()), outcome.getLoser().getName(),
                        String.valueOf(outcome.getCombatLoser()), outcome.getLoser().getName(), String.valueOf(injury)});
        details.add(detailCombat);

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
        if (outcome.getLoser().getHitpoints() > 0 && fleeChance > fleeRoll) {
            finished = true;
            NotificationDetail fleeDetail = new NotificationDetail();
            notificationService.addLocalizedTexts(fleeDetail.getText(), "detail.combat.flee",
                    new String[]{outcome.getLoser().getName(), String.valueOf(fleeRoll), String.valueOf((int) fleeChance)});

            details.add(fleeDetail);
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
        if (character.getTraits().containsKey(TraitName.FIGHTER) && character.getWeapon() != null
                && character.getWeapon().getDetails().getType() == WeaponType.MELEE) {
            log.trace("Checking fighter trait bonus.");
            if (randomService.getRandomInt(1, 100) < 50) {
                NotificationDetail detail = new NotificationDetail();
                notificationService
                        .addLocalizedTexts(detail.getText(), "detail.trait.fighter", new String[]{character.getName()});
                notification.getDetails().add(detail);

                return character.getTraits().get(TraitName.FIGHTER).getDetails().getBonus();
            }
        }

        if (character.getTraits().containsKey(TraitName.SHARPSHOOTER) && character.getWeapon() != null
                && character.getWeapon().getDetails().getType() == WeaponType.RANGED) {
            log.trace("Checking sharpshooter trait bonus.");
            if (randomService.getRandomInt(1, 100) < 50) {
                NotificationDetail detail = new NotificationDetail();
                notificationService
                        .addLocalizedTexts(detail.getText(), "detail.trait.sharpshooter", new String[]{character.getName()});
                notification.getDetails().add(detail);

                return character.getTraits().get(TraitName.SHARPSHOOTER).getDetails().getBonus();
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
