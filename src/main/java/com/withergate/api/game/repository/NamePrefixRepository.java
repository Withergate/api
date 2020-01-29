package com.withergate.api.game.repository;

import com.withergate.api.game.model.character.NamePrefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * NamePrefix repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface NamePrefixRepository extends JpaRepository<NamePrefix, String> {

}
