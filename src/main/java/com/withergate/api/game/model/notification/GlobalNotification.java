package com.withergate.api.game.model.notification;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToMany
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "global_notification")
    private Map<String, LocalizedText> message;

    @Column(name = "active")
    private boolean active;

    /**
     * Enforces single entity per table.
     */
    public enum Singleton {
        SINGLE
    }

}
