package com.withergate.api.service;

import com.withergate.api.game.model.type.AttributeTemplate;
import com.withergate.api.game.model.type.AttributeTemplate.Type;
import com.withergate.api.game.repository.clan.AttributeTemplateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RandomServiceTest {

    private RandomServiceImpl randomService;

    @Mock
    private AttributeTemplateRepository templateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        randomService = new RandomServiceImpl(templateRepository);
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
        AttributeTemplate template = randomService.getRandomAttributeCombination(sum, Type.RANDOM);

        // then verify sum matching
        Assert.assertEquals(sum, template.getCombat() + template.getScavenge()
                + template.getCraftsmanship() + template.getIntellect());
    }

}
