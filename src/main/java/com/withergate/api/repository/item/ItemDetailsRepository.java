package com.withergate.api.repository.item;

import java.util.List;

import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ItemDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetails, String> {

    List<ItemDetails> findItemDetailsByRarityAndItemType(ItemDetails.Rarity rarity, ItemType itemType);

}
