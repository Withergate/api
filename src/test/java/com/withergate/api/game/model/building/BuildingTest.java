package com.withergate.api.game.model.building;

import org.junit.Assert;
import org.junit.Test;

public class BuildingTest {

    @Test
    public void testGivenBuildingWhenGettingVisitCostThenVerifyCorrectCostReturned() {
        // given building
        BuildingDetails details = new BuildingDetails();
        details.setVisitJunkCost(10);
        Building building = new Building();
        building.setDetails(details);

        // when getting junk cost then verify cost returned based on level
        building.setLevel(0);
        Assert.assertEquals(10, building.getVisitJunkCost());
        building.setLevel(1);
        Assert.assertEquals(10, building.getVisitJunkCost());
        building.setLevel(2);
        Assert.assertEquals(8, building.getVisitJunkCost());
    }

}
