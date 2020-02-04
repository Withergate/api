package com.withergate.api.service.encounter;

import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.Gender;
import com.withergate.api.game.model.encounter.SolutionCondition;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemCost;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.service.exception.InvalidActionException;
import org.junit.Test;

public class ConditionValidatorTest {

    @Test(expected = InvalidActionException.class)
    public void testGivenCharacterWithoutItemsWhenCheckingItemConditionThenExpectException() throws Exception {
        // given character
        Character character = new Character();

        // when checking condition
        ConditionValidator.checkActionCondition(character, null, ItemCost.ANY);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenCharacterWithoutWeaponWhenCheckingWeaponConditionThenExpectException() throws Exception {
        // given character
        Character character = new Character();

        // when checking condition
        ConditionValidator.checkActionCondition(character, null, ItemCost.WEAPON);

        // then expect exception
    }

    @Test
    public void testGivenCharacterWithGearWhenCheckingGearConditionThenExpectNoException() throws Exception {
        // given character
        Character character = new Character();
        ItemDetails details = new ItemDetails();
        details.setItemType(ItemType.GEAR);
        Item item = new Item();
        item.setDetails(details);
        character.getItems().add(item);

        // when checking condition
        ConditionValidator.checkActionCondition(character, null, ItemCost.GEAR);

        // then expect no exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenMaleCharacterWhenCheckingFemaleConditionThenExpectException() throws Exception {
        // given character
        Character character = new Character();
        character.setGender(Gender.MALE);

        // when checking condition
        ConditionValidator.checkActionCondition(character, SolutionCondition.FEMALE_CHARACTER, null);

        // then expect exception
    }

}
