package com.withergate.api.profile.repository;

import java.util.List;

import com.withergate.api.profile.model.HistoricalResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Historical result repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface HistoricalResultRepository extends JpaRepository<HistoricalResult, Long> {

    List<HistoricalResult> findAllByProfileId(int profileId);
    Page<HistoricalResult> findAllByProfileId(int profileId, Pageable pageable);

}
