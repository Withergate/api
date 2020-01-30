package com.withergate.api.profile.repository;

import java.util.List;

import com.withergate.api.profile.model.HistoricalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Historical result repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface HistoricalResultRepository extends JpaRepository<HistoricalResult, Long> {

    List<HistoricalResult> findAllByPlayerId(int playerId);

}
