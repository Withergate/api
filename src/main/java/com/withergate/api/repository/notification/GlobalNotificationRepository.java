package com.withergate.api.repository.notification;

import com.withergate.api.model.notification.GlobalNotification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GlobalNotification repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface GlobalNotificationRepository extends JpaRepository<GlobalNotification, GlobalNotification.Singleton> {

}
