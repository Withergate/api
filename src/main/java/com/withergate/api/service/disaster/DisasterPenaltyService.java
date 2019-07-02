package com.withergate.api.service.disaster;

import com.withergate.api.model.Clan;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.notification.ClanNotification;

/**
 * Disaster penalty service interface. Used for computing disaster penalties for provided clan.
 *
 * @author Martin Myslik
 */
public interface DisasterPenaltyService {

    void handleDisasterPenalties(Clan clan, ClanNotification notification, Disaster disaster);

}
