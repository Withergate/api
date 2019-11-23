package com.withergate.api.repository.item;

import com.withergate.api.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Weapon repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
