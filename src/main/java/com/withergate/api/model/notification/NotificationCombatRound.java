package com.withergate.api.model.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Notification combat summary. Used only in scope of notification details.
 *
 * @author Martin Myslik
 */
@Entity
@NoArgsConstructor
@Table(name = "notification_combat_rounds")
@Getter
@Setter
public class NotificationCombatRound {

    @Id
    @Column(name = "detail_combat_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "round")
    private int round;

    @Column(name = "name_1")
    private String name1;

    @Column(name = "name_2")
    private String name2;

    @Column(name = "combat_1")
    private int combat1;

    @Column(name = "combat_2")
    private int combat2;

    @Column(name = "health_1")
    private int health1;

    @Column(name = "health_2")
    private int health2;

    @Column(name = "armor")
    private int armor;

    @Column(name = "roll_1")
    private int roll1;

    @Column(name = "roll_2")
    private int roll2;

    @Column(name = "injury")
    private int injury;

    @Column(name = "name_loser")
    private String loser;

    @JsonIgnore
    @OneToOne
    @MapsId
    private NotificationDetail detail;

    public NotificationCombatRound(NotificationCombatRound other, NotificationDetail targetDetail) {
        this.setRound(other.getRound());
        this.setName1(other.getName1());
        this.setName2(other.getName2());
        this.setLoser(other.getLoser());
        this.setInjury(other.getInjury());
        this.setArmor(other.getArmor());
        this.setCombat1(other.getCombat1());
        this.setCombat2(other.getCombat2());
        this.setRoll1(other.getRoll1());
        this.setRoll2(other.getRoll2());
        this.setHealth1(other.getHealth1());
        this.setHealth2(other.getHealth2());

        this.detail = targetDetail;
    }

}
