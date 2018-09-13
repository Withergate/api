package com.withergate.api.repository.item;

import com.withergate.api.model.item.Consumable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Consumable repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ConsumableRepository extends JpaRepository<Consumable, Integer> {
}
