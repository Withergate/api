package com.withergate.api.game.repository;

import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.encounter.Encounter;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Encounter repository.
 *
 * @author Martin Myslik
 */
public interface EncounterRepository extends JpaRepository<Encounter, Integer> {

    List<Encounter> findAllByLocationAndTurnLessThan(Location location, int turn);
}
