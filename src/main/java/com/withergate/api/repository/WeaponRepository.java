package com.withergate.api.repository;

import com.withergate.api.model.item.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Weapon repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Integer> {
}
