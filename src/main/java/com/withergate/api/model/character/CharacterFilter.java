package com.withergate.api.model.character;

import com.withergate.api.model.action.TavernAction;

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
    private Set<String> names;
    private TavernAction.Type characterType;

    public CharacterFilter() {
        avatars = new HashSet<>();
        names = new HashSet<>();
        characterType = TavernAction.Type.VETERAN;
    }

}
