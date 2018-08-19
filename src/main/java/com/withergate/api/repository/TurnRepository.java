package com.withergate.api.repository;

import com.withergate.api.model.turn.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Turn repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface TurnRepository extends JpaRepository<Turn, Integer> {

    Turn findFirstByOrderByTurnIdDesc();

}
