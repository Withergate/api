package com.withergate.api.game.repository.building;

import com.withergate.api.game.model.building.BuildingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BuildingDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface BuildingDetailsRepository extends JpaRepository<BuildingDetails, String> {

}
