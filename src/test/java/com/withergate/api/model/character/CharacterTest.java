package com.withergate.api.model.character;

import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharacterTest {

    @Test
    public void testGivenCharacterWithWeaponWhenCalculatingTotalCombatThenReturnCorrectValue() {
        // given character
        Character character = new Character();
        character.setName("Rusty Nick");
        character.setCombat(3);

        WeaponDetails details = new WeaponDetails();
        details.setName("Knife");
        details.setCombat(1);
        Weapon weapon = new Weapon();
        weapon.setWeaponDetails(details);
        character.setWeapon(weapon);

        // when getting total combat
        int result = character.getTotalCombat();

        // then verify correct value returned
        assertEquals(4, result);
    }
}
