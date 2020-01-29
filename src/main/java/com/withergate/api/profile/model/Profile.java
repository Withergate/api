package com.withergate.api.profile.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @Column(name = "name", nullable = false)
    private String name;

}
