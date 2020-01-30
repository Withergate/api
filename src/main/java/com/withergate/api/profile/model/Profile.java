package com.withergate.api.profile.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "ranking")
    private int ranking;

    @Enumerated(EnumType.STRING)
    @Column(name = "premium_type")
    private PremiumType premiumType;

}
