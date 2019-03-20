package com.withergate.api.repository;

import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
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
