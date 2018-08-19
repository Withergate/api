package com.withergate.api.repository;

import com.withergate.api.model.character.NamePrefix;
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
