package com.withergate.api.repository.item;

import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.OutfitDetails;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OutfitDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface OutfitDetailsRepository extends JpaRepository<OutfitDetails, String> {

    List<OutfitDetails> findAllByRarity(ItemDetails.Rarity rarity);
}
