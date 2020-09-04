package com.withergate.api.service.encounter;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.Gender;
import com.withergate.api.game.model.encounter.SolutionCondition;
import com.withergate.api.game.model.item.ItemCost;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;

/**
 * Class used for validating conditions.
 *
 * @author Martin Myslik
 */
@Slf4j
public class ConditionValidator {

    private ConditionValidator() {
        // disabled constructor
    }

    /**
     * Checks the condition and throws an exception if it is not met.
     *
     * @param character character
     * @param condition condition
     */
    public static void checkActionCondition(Character character, SolutionCondition condition, ItemCost itemCost)
            throws InvalidActionException {
        if (itemCost != null) {
            switch (itemCost) {
                case ANY:
                    if (character.getItems().isEmpty()) {
                        throw new InvalidActionException("error.constraint-item", "Character must carry at least one item!");
                    }
                    break;
                case WEAPON:
                    if (character.getWeapon() == null) {
                        throw new InvalidActionException("error.constraint-weapon", "Character must carry a weapon!");
                    }
                    break;
                case OUTFIT:
                    if (character.getOutfit() == null) {
                        throw new InvalidActionException("error.constraint-outfit", "Character must carry an outfit!");
                    }
                    break;
                case GEAR:
                    if (character.getGear() == null) {
                        throw new InvalidActionException("error.constraint-gear", "Character must carry a gear!");
                    }
                    break;
                default:
                    log.error("Unknown item prereq: {}", itemCost);
            }
        }

        if (condition != null) {
            switch (condition) {
                case FEMALE_CHARACTER:
                    if (!character.getGender().equals(Gender.FEMALE)) {
                        throw new InvalidActionException("error.constraint-female", "Character must be FEMALE to perform this action.");
                    }
                    break;
                case HEALTHY_CHARACTER:
                    if (character.getHitpoints() < character.getMaxHitpoints()) {
                        throw new InvalidActionException("error.constraint-health", "Character must be healthy to perform this action.");
                    }
                    break;
                default:
                    log.error("Unknown quest condition: {}.", condition);
            }
        }
    }

}
