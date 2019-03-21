package com.withergate.api.model.notification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clan_notifications")
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

    @Column(name = "result")
    private String result;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id")
    private List<NotificationDetail> details;

    // INCOME

    @Column(name = "junk_income")
    private int junkIncome;

    @Column(name = "food_income")
    private int foodIncome;

    @Column(name = "caps_income")
    private int capsIncome;

    @Column(name = "fameIncome")
    private int fameIncome;

    @Column(name = "injury")
    private int injury;

    @Column(name = "healing")
    private int healing;

    @Column(name = "experience")
    private int experience;

    public ClanNotification() {
        if (details == null) details = new ArrayList<>();
    }

}
