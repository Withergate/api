package com.withergate.api.profile.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.withergate.api.profile.model.achievement.Achievement;
import lombok.Getter;
import lombok.Setter;

/**
 * Profile entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "profiles")
@Getter
@Setter
public class Profile {

    /**
     * Profile ID is set manually and matches the authenticated user's ID.
     */
    @Id
    @Column(name = "profile_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "theme")
    private String theme;

    @Column(name = "ranking")
    private int ranking;

    @Enumerated(EnumType.STRING)
    @Column(name = "premium_type")
    private PremiumType premiumType;

    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @Column(name = "consecutive_logins")
    private int consecutiveLogins;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HistoricalResult> results;

    @OneToMany(mappedBy = "profile", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Achievement> achievements;

    /**
     * Constructor.
     */
    public Profile() {
        this.results = new ArrayList<>();
    }

    @JsonProperty("numGames")
    public int getNumPlayedGames() {
        return results.size();
    }

}
