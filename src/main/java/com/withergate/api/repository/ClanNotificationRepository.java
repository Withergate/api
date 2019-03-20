package com.withergate.api.repository;

import com.withergate.api.model.notification.ClanNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClanNotification repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ClanNotificationRepository extends JpaRepository<ClanNotification, Long> {

    List<ClanNotification> findAllByClanId(int clanId);

    List<ClanNotification> findAllByClanIdAndTurnId(int clanId, int turnId);

}
