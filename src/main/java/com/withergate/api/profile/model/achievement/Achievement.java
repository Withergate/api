package com.withergate.api.profile.model.achievement;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.research.ResearchDetails;
import com.withergate.api.profile.model.Profile;
import lombok.Getter;
import lombok.Setter;

/**
 * Achievement entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "achievements")
@Getter
@Setter
public class Achievement {

    @Id
    @Column(name = "achievement_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private AchievementDetails details;

    @Column(name = "award_date", updatable = false, nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

}
