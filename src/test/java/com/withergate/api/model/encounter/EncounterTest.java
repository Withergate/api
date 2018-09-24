package com.withergate.api.model.encounter;

import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Gender;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncounterTest {

    @Test
    public void testGivenEncounterWhenEnhancingTestThenVerifyNameInjected() {
        // given encounter
        Encounter encounter = new Encounter();
        encounter.setDescriptionText("This is [CH].");

        // when getting enhanced text
        Character character = new Character();
        character.setName("Rusty Nick");

        String result = encounter.getDescriptionText(character, Location.WASTELAND);

        // then verify correct text returned
        assertEquals("This is [Rusty Nick].", result);
    }

    @Test
    public void testGivenEncounterWhenEnhancingTestThenAllValuesInjected() {
        // given encounter
        Encounter encounter = new Encounter();
        encounter.setDescriptionText("[G1] went to [L] and although [G2] name is [CH], everybody called [G3] John.");

        // when getting enhanced text
        Character character = new Character();
        character.setGender(Gender.MALE);
        character.setName("Rusty Nick");

        String result = encounter.getDescriptionText(character, Location.WASTELAND);

        // then verify correct text returned
        assertEquals("He went to wasteland and although his name is [Rusty Nick], everybody called him John.", result);
    }
}
