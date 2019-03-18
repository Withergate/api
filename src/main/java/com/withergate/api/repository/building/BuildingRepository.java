package com.withergate.api.repository.building;

import com.withergate.api.model.building.Building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Building repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {

}
