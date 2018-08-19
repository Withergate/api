package com.withergate.api.repository;

import com.withergate.api.model.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Clan repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ClanRepository extends JpaRepository<Clan, Integer> {

    Clan findOneByName(String name);
}
