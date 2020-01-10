package com.withergate.api.repository.clan;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.faction.Faction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Clan> findAll(Pageable pageable);

    long count();

}
