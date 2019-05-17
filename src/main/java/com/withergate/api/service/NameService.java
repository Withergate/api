package com.withergate.api.service;

import java.util.Set;

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
     * @param nameFilter names to be filtered out to avoid duplicities
     * @return the generated name
     */
    String generateRandomName(Gender gender, Set<String> nameFilter);

    /**
     * Generates a random avatar for the specified gender.
     *
     * @param gender the specified gender
     * @param avatarFilter avatars to be filtered out to avoid duplicities
     * @return the generated avatar URL
     */
    String generateRandomAvatar(Gender gender, Set<String> avatarFilter);
}
