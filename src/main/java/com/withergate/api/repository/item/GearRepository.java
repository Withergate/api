package com.withergate.api.repository.item;

import com.withergate.api.model.item.Gear;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Gear repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface GearRepository extends JpaRepository<Gear, Integer> {
}
