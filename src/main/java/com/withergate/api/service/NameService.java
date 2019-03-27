package com.withergate.api.service;

import com.withergate.api.model.character.Gender;

/**
 * Name service interface.
 *
 * @author Martin Myslik
 */
public interface NameService {

    /**
     * Generates a random name for the specified gender.
     *
     * @param gender the specified gender
     * @return the generated name
     */
    String generateRandomName(Gender gender);

    /**
     * Generates a random avatar for the specified gender.
     *
     * @param gender the specified gender
     * @return the generated avatar URL
     */
    String generateRandomAvatar(Gender gender);
}
