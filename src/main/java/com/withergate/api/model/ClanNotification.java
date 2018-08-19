package com.withergate.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player_notifications")
@Getter
@Setter
public class ClanNotification {

    @Id
    @Column(name = "notification_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "clan_id", updatable = false, nullable = false)
    private int clanId;

    @Column(name = "turn_id", updatable = false, nullable = false)
    private int turnId;

    @Column(name = "text", updatable = false, nullable = false)
    private String text;
}
