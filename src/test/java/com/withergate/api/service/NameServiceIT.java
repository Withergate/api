package com.withergate.api.service;

import com.withergate.api.model.character.Gender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NameServiceIT {

    @Autowired
    private NameService nameService;

    @Test
    public void testGivenGenderWhenGeneratingRandomNameThenVerifyNameContainsSpace() {
        // given gender
        Gender gender = Gender.FEMALE;

        // when generating random name
        String name = nameService.generateRandomName(gender);

        // then verify name contains space\
        assertTrue(name.contains(" "));
    }
}
