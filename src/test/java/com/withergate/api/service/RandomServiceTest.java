package com.withergate.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RandomServiceTest {

    private RandomServiceImpl randomService;

    @Before
    public void setUp() {
        randomService = new RandomServiceImpl();
    }

    @Test
    public void testGivenRangeWhenGettingRandomNumberThenVerifyRangeNotExceeded() {
        // given range
        int lower = 1;
        int higher = 3;

        // when getting random number
        int result = randomService.getRandomInt(lower, higher);

        // then verify range no exceeded
        Assert.assertTrue(result <= higher);
        Assert.assertTrue(result >= lower);
    }

}
