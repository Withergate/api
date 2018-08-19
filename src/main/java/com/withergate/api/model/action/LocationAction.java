package com.withergate.api.model.action;

import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Location action. Used for sending a character to a specified location.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "location_actions")
@Getter
@Setter
public class LocationAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="character_id", unique = true, nullable = false, updatable = false)
    private Character character;

    @Column(name = "location", nullable = false)
    @Enumerated(EnumType.STRING)
    private Location location;

}
