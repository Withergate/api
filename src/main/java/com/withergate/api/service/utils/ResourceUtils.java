package com.withergate.api.service.utils;

import java.util.Optional;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.statistics.FameStatistics;
import lombok.experimental.UtilityClass;

/**
 * Resource utils. Used for updating resources for clan and notification entities.
 *
 * @author Martin Myslik
 */
@UtilityClass
public class ResourceUtils {

    /**
     * Updates fame.
     *
     * @param fame fame
     * @param source source of fame
     * @param clan clan
     * @param notification notification
     */
    public void changeFame(int fame, String source, Clan clan, ClanNotification notification) {
        clan.setFame(clan.getFame() + fame);
        if (notification != null) {
            notification.setFameIncome(notification.getFameIncome() + fame);
        }

        Optional<FameStatistics> opStats = clan.getFameStatistics().stream().filter(s -> s.getName().equals(source)).findFirst();
        if (opStats.isEmpty()) {
            FameStatistics statistics = new FameStatistics();
            statistics.setName(source);
            statistics.setFame(fame);
            statistics.setClan(clan);
            clan.getFameStatistics().add(statistics);
        } else {
            opStats.get().setFame(opStats.get().getFame() + fame);
        }
    }

    /**
     * Updates food.
     *
     * @param food food
     * @param clan clan
     * @param notification notification
     */
    public void changeFood(int food, Clan clan, ClanNotification notification) {
        clan.setFood(clan.getFood() + food);
        if (notification != null) {
            notification.setFoodIncome(notification.getFoodIncome() + food);
        }
    }

    /**
     * Updates junk.
     *
     * @param junk junk
     * @param clan clan
     * @param notification notification
     */
    public void changeJunk(int junk, Clan clan, ClanNotification notification) {
        clan.setJunk(clan.getJunk() + junk);
        if (notification != null) {
            notification.setJunkIncome(notification.getJunkIncome() + junk);
        }
    }

    /**
     * Updates caps.
     *
     * @param caps caps
     * @param clan clan
     * @param notification notification
     */
    public void changeCaps(int caps, Clan clan, ClanNotification notification) {
        clan.setCaps(clan.getCaps() + caps);
        if (notification != null) {
            notification.setCapsIncome(notification.getCapsIncome() + caps);
        }
    }

    /**
     * Updates information.
     *
     * @param information information
     * @param clan clan
     * @param notification notification
     */
    public void changeInformation(int information, Clan clan, ClanNotification notification) {
        clan.setInformation(clan.getInformation() + information);
        if (notification != null) {
            notification.setInformation(notification.getInformation() + information);
        }
    }

    /**
     * Updates faction points.
     *
     * @param points points
     * @param clan clan
     * @param notification notification
     */
    public void changeFactionPoints(int points, Clan clan, ClanNotification notification) {
        clan.setFactionPoints(clan.getFactionPoints() + points);
        if (notification != null) {
            notification.setFactionPoints(notification.getFactionPoints() + points);
        }
    }

}
