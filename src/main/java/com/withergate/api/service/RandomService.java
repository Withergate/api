package com.withergate.api.service;

import com.withergate.api.model.character.Gender;

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
}
