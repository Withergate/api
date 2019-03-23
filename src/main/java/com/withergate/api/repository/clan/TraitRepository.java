package com.withergate.api.repository.clan;

import com.withergate.api.model.character.Trait;

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
