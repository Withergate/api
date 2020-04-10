package com.withergate.api.game.repository.notification;

import com.withergate.api.game.model.notification.GlobalNotification;

import com.withergate.api.game.model.notification.GlobalNotification.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GlobalNotification repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface GlobalNotificationRepository extends JpaRepository<GlobalNotification, Type> {

}
