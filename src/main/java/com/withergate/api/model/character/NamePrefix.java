package com.withergate.api.model.character;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Name prefix entity. Used for random name generation.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "name_prefixes")
@Getter
@Setter
public class NamePrefix {

    @Id
    @Column(name = "value", nullable = false)
    private String value;

}
