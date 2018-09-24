package com.withergate.api.repository;

import com.withergate.api.model.Location;
import com.withergate.api.model.encounter.Encounter;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Encounter repository.
 *
 * @author Martin Myslik
 */
public interface EncounterRepository extends JpaRepository<Encounter, Integer> {

    List<Encounter> findAllByLocation(Location location);
}
