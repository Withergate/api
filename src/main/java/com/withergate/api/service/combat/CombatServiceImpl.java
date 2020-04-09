package com.withergate.api.service.combat;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.game.model.type.AttributeTemplate.Type;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterFilter;
import com.withergate.api.game.model.combat.CombatResult;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.location.ArenaResult;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
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

    private static final int ARENA_CAPS = 15;
    private static final int ARENA_CAPS_LOSER = 5;
    private static final int ARENA_FAME = 5;
    private static final String NPC = "NPC";

    private final CombatRoundService combatRoundService;
    private final RandomService randomService;
    private final CharacterService characterService;
    private final NotificationService notificationService;
    private final ItemService itemService;

    @Override
    public boolean handleSingleCombat(ClanNotification notification, int difficulty, Character character) {
        log.debug("Handling encounter combat for character {} and encounter difficulty {}.", character.getName(),
                difficulty);

        // prepare enemy
        Character enemy = new Character();
        enemy.setNpc(true);
        enemy.setId(-1);
        enemy.setName("Enemy");
        enemy.setCombat(difficulty);
        enemy.setHitpoints(RandomServiceImpl.K10 + randomService.getRandomInt(difficulty, RandomServiceImpl.K10));
        enemy.setMaxHitpoints(enemy.getHitpoints());

        CombatResult result = handleCombat(character, notification, enemy, new ClanNotification());

        // compute combat result
        return result.getWinner().getId() == character.getId();
    }

    @Override
    public boolean handleClanCombat(ClanNotification attackerNotification, ClanNotification defenderNotification,
                                    Character attacker, Character defender) {
        CombatResult result  = handleCombat(attacker, attackerNotification, defender, defenderNotification);
        if (result.getWinner().getId() == attacker.getId()) {
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
            Character character = characterService.generateRandomCharacter(new CharacterFilter(),
                    randomService.getRandomAttributeCombination(ClanServiceImpl.MAX_CHARACTER_STRENGTH, Type.RANDOM));
            character.setNpc(true);
            characters.add(character);
        }

        // split characters into fighting pairs
        for (int i = 0; i < characters.size(); i += 2) {
            Character character1 = characters.get(i);
            Character character2 = characters.get(i + 1);

            // process fight
            ClanNotification notification1 = new ClanNotification();
            ClanNotification notification2 = new ClanNotification();
            unequipRangedWeapon(character1, notification1);
            unequipRangedWeapon(character2, notification2);

            // compute fight results
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

        int round = 1;
        while (true) {
            CombatResult result = combatRoundService.handleCombatRound(character1, notification1, character2, notification2, round);

            // add details to both players
            for (NotificationDetail detail : result.getDetails()) {
                notification1.getDetails().add(new NotificationDetail(detail));
                notification2.getDetails().add(new NotificationDetail(detail));
            }

            if (result.isFinished()) {
                // handle bonuses
                if (!result.getWinner().isNpc()) {
                    Character winner = result.getWinner();
                    ClanNotification winnerNotification = winner.getId() == character1.getId() ? notification1 : notification2;
                    handleWinnerBonuses(result.getWinner(), winnerNotification);
                }

                return result;
            }

            round++;
        }
    }

    private ArenaResult getWinnerResult(Character character, Character opponent, ClanNotification notification) {
        log.debug("{} won the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) return null;

        notification.setClanId(character.getClan().getId());
        notification.setHeader(character.getName());
        notification.setImageUrl(character.getImageUrl());
        notificationService.addLocalizedTexts(notification.getText(), "combat.arena.description",
                new String[]{character.getName(), character.getClan().getName(),
                        opponent.getName(), opponent.getClan() != null ? opponent.getClan().getName() : NPC});
        notificationService
                .addLocalizedTexts(notification.getText(), "combat.arena.win", new String[]{character.getName()});

        character.getClan().changeCaps(ARENA_CAPS); // add caps to the winner
        character.getClan().changeFame(ARENA_FAME); // add fame to the winner
        notification.changeCaps(ARENA_CAPS);
        notification.changeFame(ARENA_FAME);

        // handle experience
        character.changeExperience(2);
        notification.changeExperience(2);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setWinner(true);
        result.setNotification(notification);

        return result;
    }

    private ArenaResult getLoserResult(Character character, Character opponent, ClanNotification notification) {
        log.debug("{} lost the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) return null;

        notification.setClanId(character.getClan().getId());
        notification.setHeader(character.getName());
        notification.setImageUrl(character.getImageUrl());
        notificationService.addLocalizedTexts(notification.getText(), "combat.arena.description",
                new String[]{character.getName(), character.getClan().getName(),
                        opponent.getName(), opponent.getClan() != null ? opponent.getClan().getName() : NPC});
        notificationService
                .addLocalizedTexts(notification.getText(), "combat.arena.lose", new String[]{character.getName()});

        character.getClan().changeCaps(ARENA_CAPS_LOSER); // add caps to the loser
        notification.changeCaps(ARENA_CAPS_LOSER);

        // handle experience
        character.changeExperience(1);
        notification.changeExperience(1);

        ArenaResult result = new ArenaResult();
        result.setCharacter(character);
        result.setWinner(false);
        result.setNotification(notification);

        return result;
    }

    private void unequipRangedWeapon(Character character, ClanNotification notification) {
        if (character.getWeapon() != null && character.getWeapon().getDetails().getWeaponType().equals(ItemDetails.WeaponType.RANGED)) {
            try {
                itemService.unequipItem(character.getWeapon().getId(), character.getId(), character.getClan().getId());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.arena.unequip", new String[]{character.getName()});
                notification.getDetails().add(detail);
            } catch (InvalidActionException e) {
                log.error("Error un-equipping ranged weapon before arena fight.");
            }
        }
    }

    private void handleWinnerBonuses(Character character, ClanNotification notification) {
        Research research = character.getClan().getResearch(ResearchBonusType.COMBAT_FAME);
        if (research != null && research.isCompleted()) {
            // add fame to clan
            notification.changeFame(research.getDetails().getValue());
            character.getClan().changeFame(research.getDetails().getValue());

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), research.getDetails().getBonusText(), new String[]{});
            notification.getDetails().add(detail);
        }
    }

}
