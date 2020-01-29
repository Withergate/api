package com.withergate.api.game.repository.item;

import com.withergate.api.game.model.item.Item;
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
