package com.withergate.api.model.encounter;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Gender;
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
    public static void checkQuestCondition(Character character, SolutionCondition condition) throws InvalidActionException {
        if (condition == null) {
            return;
        }

        switch (condition) {
            case FEMALE_CHARACTER:
                if (!character.getGender().equals(Gender.FEMALE)) {
                    throw new InvalidActionException("Character must be FEMALE to perform this action.");
                }
                break;
            case HEALTHY_CHARACTER:
                if (character.getHitpoints() < character.getMaxHitpoints()) {
                    throw new InvalidActionException("Character must be healthy to perform this action.");
                }
                break;
            case ITEM_EQUIPPED:
                if (character.getItems().isEmpty()) {
                    throw new InvalidActionException("Character must carry at least one item.");
                }
                break;
            default:
                log.error("Unknown quest condition: {}.", condition);
        }
    }

}
