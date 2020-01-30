package com.withergate.api.game.repository.clan;

import java.util.List;

import com.withergate.api.game.model.Clan;
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

    List<Clan> findAllByOrderByFameDesc();

    long count();

}
