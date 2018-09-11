package com.withergate.api.service.encounter;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.service.IRandomService;
import com.withergate.api.service.RandomService;
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

    /**
     * Constructor.
     *
     * @param randomService random service
     */
    public CombatService(IRandomService randomService) {
        this.randomService = randomService;
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
