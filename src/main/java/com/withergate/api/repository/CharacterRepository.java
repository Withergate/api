package com.withergate.api.repository;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Character repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {

    List<Character> findAllByState(CharacterState state);
}
