package com.withergate.api.game.repository.statistics;

import java.util.List;

import com.withergate.api.game.model.statistics.ClanTurnStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Clan turn statistics repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ClanTurnStatisticsRepository extends JpaRepository<ClanTurnStatistics, Integer> {

    List<ClanTurnStatistics> findAllByClanIdOrderByTurnIdAsc(int clanId);

}
