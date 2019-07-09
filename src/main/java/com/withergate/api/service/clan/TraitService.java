package com.withergate.api.service.clan;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;

/**
 * Trait service.
 *
 * @author Martin Myslik
 */
public interface TraitService {

    Trait getRandomTrait(Character character);
}
