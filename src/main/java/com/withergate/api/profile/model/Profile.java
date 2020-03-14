package com.withergate.api.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.game.model.view.Views;
import com.withergate.api.profile.model.achievement.Achievement;
import com.withergate.api.profile.model.achievement.Rarity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    @JsonView(Views.Public.class)
    @Column(name = "profile_id")
    private int id;

    @JsonView(Views.Public.class)
    @Column(name = "name")
    private String name;

    @JsonView(Views.Public.class)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @JsonView(Views.Internal.class)
    @Column(name = "theme")
    private String theme;

    @JsonView(Views.Public.class)
    @Column(name = "ranking")
    private int ranking;

    @JsonView(Views.Public.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "premium_type")
    private PremiumType premiumType;

    @JsonView(Views.Public.class)
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    @JsonView(Views.Internal.class)
    @Column(name = "consecutive_logins")
    private int consecutiveLogins;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<HistoricalResult> results;

    @JsonView(Views.Internal.class)
    @OneToMany(mappedBy = "profile", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Achievement> achievements;

    /**
     * Constructor.
     */
    public Profile() {
        this.results = new HashSet<>();
        this.achievements = new HashSet<>();
    }

    @JsonView(Views.Public.class)
    @JsonProperty("numGames")
    public int getNumPlayedGames() {
        return results.size();
    }

    /**
     * Compiles stats about achievements.
     */
    @JsonView(Views.Public.class)
    @JsonProperty("achievementStats")
    public Map<Rarity, Integer> getAchievementStats() {
        Map<Rarity, Integer> stats = new HashMap<>();
        Arrays.asList(Rarity.values()).forEach(r -> stats.put(r, 0));
        this.achievements.forEach(a -> stats.put(a.getDetails().getRarity(), stats.get(a.getDetails().getRarity()) + 1));
        return stats;
    }

}
