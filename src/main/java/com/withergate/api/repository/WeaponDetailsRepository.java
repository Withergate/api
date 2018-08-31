package com.withergate.api.repository;

import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * WeaponDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface WeaponDetailsRepository extends JpaRepository<WeaponDetails, String> {
}
