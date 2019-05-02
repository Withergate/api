package com.withergate.api.model.notification;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification detail. Used only in scope of clan notifications.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "notification_details")
@Getter
@Setter
public class NotificationDetail {

    @Id
    @Column(name = "detail_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "notification_detail_id")
    private Map<String, LocalizedText> text;

    public NotificationDetail() {
        if (text == null) text = new HashMap<>();
    }

}
