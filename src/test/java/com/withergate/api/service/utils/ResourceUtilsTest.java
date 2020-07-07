package com.withergate.api.service.utils;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.notification.ClanNotification;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ResourceUtilsTest {

    @Test
    public void testGivenClanAndNotificationWhenChangingResourcesThenVerifyEntitiesUpdated() {
        // given clan and notification
        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(50);
        clan.setFood(20);
        clan.setJunk(10);
        clan.setInformation(5);
        clan.setFame(100);
        clan.setFactionPoints(0);

        ClanNotification notification = new ClanNotification();

        // when changing resources
        ResourceUtils.changeCaps(10, clan, notification);
        ResourceUtils.changeFood(5, clan, notification);
        ResourceUtils.changeJunk(- 5, clan, notification);
        ResourceUtils.changeInformation(1, clan, notification);
        ResourceUtils.changeFame(20, "SOURCE", clan, notification);
        ResourceUtils.changeFactionPoints(10, clan, notification);

        // then verify entities updated
        Assert.assertEquals(60, clan.getCaps());
        Assert.assertEquals(25, clan.getFood());
        Assert.assertEquals(5, clan.getJunk());
        Assert.assertEquals(6, clan.getInformation());
        Assert.assertEquals(120, clan.getFame());
        Assert.assertEquals(10, clan.getFactionPoints());

        Assert.assertEquals(10, notification.getCapsIncome());
        Assert.assertEquals(5, notification.getFoodIncome());
        Assert.assertEquals(- 5, notification.getJunkIncome());
        Assert.assertEquals(1, notification.getInformation());
        Assert.assertEquals(20, notification.getFameIncome());
        Assert.assertEquals(10, notification.getFactionPoints());
    }

}
