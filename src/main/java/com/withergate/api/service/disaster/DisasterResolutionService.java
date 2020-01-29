package com.withergate.api.service.disaster;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.disaster.Disaster;
import com.withergate.api.game.model.notification.ClanNotification;

/**
 * Disaster penalty service interface. Used for computing disaster penalties for provided clan.
 *
 * @author Martin Myslik
 */
public interface DisasterResolutionService {

    void handleDisasterResolution(Clan clan, ClanNotification notification, Disaster disaster);

}
