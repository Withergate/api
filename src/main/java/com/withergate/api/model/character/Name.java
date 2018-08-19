package com.withergate.api.model.character;

import com.withergate.api.model.character.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "names")
@Getter
@Setter
public class Name {

    @Id
    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
