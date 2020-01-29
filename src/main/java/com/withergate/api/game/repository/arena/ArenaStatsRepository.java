package com.withergate.api.game.repository.arena;

import java.util.List;

import com.withergate.api.game.model.arena.ArenaStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Arena stats repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ArenaStatsRepository extends JpaRepository<ArenaStats, Integer> {

    List<ArenaStats> findTop10ByOrderByStatsDesc();

}
