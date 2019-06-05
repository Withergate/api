package com.withergate.api.model.character;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity used for filtering character attributes.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class CharacterFilter {

    private Set<String> avatars;
    private Set<String> names;

    public CharacterFilter() {
        avatars = new HashSet<>();
        names = new HashSet<>();
    }

}
