package com.withergate.api.game.model.cost;

import com.withergate.api.game.model.Clan;
import com.withergate.api.service.exception.InvalidActionException;
import org.junit.Assert;
import org.junit.Test;

public class ActionCostTest {

    @Test
    public void testGivenActionCostWhenPayingResourcesThenVerifyResourcesPaid() throws Exception {
        // given action cost and clan
        Clan clan = new Clan();
        clan.setCaps(50);
        clan.setJunk(20);
        clan.setFood(20);
        clan.setInformation(10);
        clan.setFactionPoints(15);

        ActionCost cost = new ActionCost(5, 10, 15, 5, 10, null, false);

        // when paying resources
        cost.payResources(clan);

        // then verify resources paid
        Assert.assertEquals(15, clan.getFood());
        Assert.assertEquals(10, clan.getJunk());
        Assert.assertEquals(35, clan.getCaps());
        Assert.assertEquals(5, clan.getInformation());
        Assert.assertEquals(5, clan.getFactionPoints());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenClanWithLowResourcesWhenPayingResourcesThenExpectException() throws Exception {
        // given action cost and clan
        Clan clan = new Clan();

        ActionCost cost = new ActionCost(5, 10, 15, 5, 10, null, false);

        // when paying resources
        cost.payResources(clan);

        // then expect exception
    }

    @Test
    public void testGivenActionCostWhenRefundingResourcesThenVerifyResourcesRefunded() throws Exception {
        // given action cost and clan
        Clan clan = new Clan();
        clan.setCaps(50);
        clan.setJunk(20);
        clan.setFood(20);
        clan.setInformation(10);
        clan.setFactionPoints(15);

        ActionCost cost = new ActionCost(5, 10, 15, 5, 10, null, false);

        // when paying resources
        cost.refundResources(clan);

        // then verify resources paid
        Assert.assertEquals(25, clan.getFood());
        Assert.assertEquals(30, clan.getJunk());
        Assert.assertEquals(65, clan.getCaps());
        Assert.assertEquals(15, clan.getInformation());
        Assert.assertEquals(25, clan.getFactionPoints());
    }

}
