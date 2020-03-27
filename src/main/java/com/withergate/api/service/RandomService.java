package com.withergate.api.service;

import com.withergate.api.game.model.type.AttributeTemplate;
import com.withergate.api.game.model.type.AttributeTemplate.Type;
import com.withergate.api.game.model.character.Gender;
import com.withergate.api.game.model.item.ItemType;

/**
 * RandomService interface. Used for generating random entities and numbers.
 *
 * @author Martin Myslik
 */
public interface RandomService {

    /**
     * Gets a random gender.
     *
     * @return random Gender
     */
    Gender getRandomGender();

    /**
     * Generates a random integer for the specified range.
     *
     * @param min min value
     * @param max max value
     * @return the generated integer
     */
    int getRandomInt(int min, int max);

    /**
     * Generates random item type.
     *
     * @return random item type
     */
    ItemType getRandomItemType();

    /**
     * Generates random attribute combination.
     *
     * @param n total sum
     * @return attribute combination
     */
    AttributeTemplate getRandomAttributeCombination(int n, Type type);

}
