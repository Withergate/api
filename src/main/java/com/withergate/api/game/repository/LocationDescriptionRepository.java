package com.withergate.api.game.repository;

import com.withergate.api.game.model.location.Location;
import com.withergate.api.game.model.location.LocationDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * LocationDescription repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface LocationDescriptionRepository extends JpaRepository<LocationDescription, Location> {
}
