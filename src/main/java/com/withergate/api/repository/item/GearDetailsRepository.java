package com.withergate.api.repository.item;

import com.withergate.api.model.item.GearDetails;
import com.withergate.api.model.item.ItemDetails;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GearDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface GearDetailsRepository extends JpaRepository<GearDetails, String> {

    List<GearDetails> findAllByRarity(ItemDetails.Rarity rarity);
}
