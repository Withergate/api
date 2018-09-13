package com.withergate.api.repository.item;

import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ConsumableDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ConsumableDetailsRepository extends JpaRepository<ConsumableDetails, String> {

    List<ConsumableDetails> findAllByRarity(Rarity rarity);
}
