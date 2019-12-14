package com.withergate.api.model.character;

import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.ItemType;
import org.junit.Assert;
import org.junit.Test;

public class CharacterTest {

    @Test
    public void testGivenCharacterWithWeaponWhenCalculatingTotalCombatThenReturnCorrectValue() {
        // given character
        Character character = new Character();
        character.setName("Rusty Nick");
        character.setCombat(3);

        ItemDetails details = new ItemDetails();
        details.setCombat(1);
        details.setItemType(ItemType.WEAPON);
        Item weapon = new Item();
        weapon.setDetails(details);
        character.getItems().add(weapon);

        // when getting total combat
        int result = character.getTotalCombat();

        // then verify correct value returned
        Assert.assertEquals(4, result);
    }

    @Test
    public void testGivenHealthyCharacterWhenGettingStatsThenVerifyStatsUnchanged() {
        // given character
        Character character = new Character();
        character.setHitpoints(10);
        character.setMaxHitpoints(10);
        character.setCombat(5);
        character.setScavenge(4);
        character.setCraftsmanship(2);
        character.setIntellect(1);

        // when getting stats verify unchanged
        Assert.assertEquals(5, character.getCombat());
        Assert.assertEquals(4, character.getScavenge());
        Assert.assertEquals(2, character.getCraftsmanship());
        Assert.assertEquals(1, character.getIntellect());
    }

    @Test
    public void testGivenInjuredCharacterWhenGettingStatsThenVerifyStatsLowered() {
        // given character
        Character character = new Character();
        character.setHitpoints(6);
        character.setMaxHitpoints(10);
        character.setCombat(5);
        character.setScavenge(4);
        character.setCraftsmanship(2);
        character.setIntellect(1);

        // when getting stats verify lowered
        Assert.assertEquals(4, character.getCombat());
        Assert.assertEquals(3, character.getScavenge());
        Assert.assertEquals(1, character.getCraftsmanship());
        Assert.assertEquals(1, character.getIntellect());
    }

    @Test
    public void testGivenHeavilyInjuredCharacterWhenGettingStatsThenVerifyStatsLowered() {
        // given character
        Character character = new Character();
        character.setHitpoints(3);
        character.setMaxHitpoints(10);
        character.setCombat(5);
        character.setScavenge(4);
        character.setCraftsmanship(2);
        character.setIntellect(1);

        // when getting stats verify lowered
        Assert.assertEquals(3, character.getCombat());
        Assert.assertEquals(2, character.getScavenge());
        Assert.assertEquals(1, character.getCraftsmanship());
        Assert.assertEquals(1, character.getIntellect());
    }
}
