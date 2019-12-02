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

    @Test
    public void testGivenSumWhenGeneratingCombinationsThenVerifySumMatching() {
        // given sum
        int sum = 16;

        // when generating combination
        int[] comb = randomService.getRandomAttributeCombination(sum);

        // then verify sum matching
        int result = 0;
        for (int i = 0; i < comb.length; i++) {
            result += comb[i];
            Assert.assertTrue(comb[i] <= 6);
        }
        Assert.assertEquals(sum, result);
    }

}
