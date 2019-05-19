package com.withergate.api.model.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Global notification entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "global_notification")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GlobalNotification {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "single_id")
    private Singleton id;

    @Column(name = "message")
    private String message;

    @Column(name = "active")
    private boolean active;

    /**
     * Enforces single entity per table.
     */
    public enum Singleton {
        SINGLE
    }

}
