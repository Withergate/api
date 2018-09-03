package com.withergate.api.model.encounter;

import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
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
}
