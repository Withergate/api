package com.withergate.api.model.character;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity used for filtering character attributes.
 */
@Getter
@Setter
public class CharacterFilter {

    private Set<String> avatars;

    public CharacterFilter() {
        avatars = new HashSet<>();
    }

}
