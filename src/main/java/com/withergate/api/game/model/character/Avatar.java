package com.withergate.api.game.model.character;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Avatar entity. Container for character images.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "avatars")
@Getter
@Setter
public class Avatar {

    @Id
    @Column(name = "avatar_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
