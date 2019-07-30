package com.withergate.api.service.combat;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.combat.CombatResult;
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

    private static final int ARENA_CAPS = 15;
    private static final int ARENA_FAME = 5;

    private final CombatRoundService combatRoundService;
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
            CombatResult result = combatRoundService.handleCombatRound(character1, notification1, character2, notification2);
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

    private ArenaResult getWinnerResult(Character character, Character opponent, ClanNotification notification) {
        log.debug("{} won the fight. Updating results...", character.getName());

        // do not handle non-player characters
        if (character.getClan() == null) return null;

        notification.setClanId(character.getClan().getId());
        notification.setHeader(character.getName());
        notification.setImageUrl(character.getImageUrl());
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
        if (character.getClan() == null) return null;

        notification.setClanId(character.getClan().getId());
        notification.setHeader(character.getName());
        notification.setImageUrl(character.getImageUrl());
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

}
