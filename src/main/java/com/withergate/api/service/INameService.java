package com.withergate.api.service;

import com.withergate.api.model.character.Gender;

/**
 * Name service interface.
 *
 * @author Martin Myslik
 */
public interface INameService {

    /**
     * Generates a random name for the specified gender.
     *
     * @param gender the specified gender
     * @return the generated name
     */
    String generateRandomName(Gender gender);
}
