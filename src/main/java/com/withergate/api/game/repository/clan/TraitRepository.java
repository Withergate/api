package com.withergate.api.game.repository.clan;

import com.withergate.api.game.model.character.Trait;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Building repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface TraitRepository extends JpaRepository<Trait, Integer> {

}
