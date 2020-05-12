package com.withergate.api.game.repository.item;

import java.util.List;

import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ItemDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetails, String> {

    List<ItemDetails> findItemDetailsByRarityAndItemTypeAndTurnLessThan(ItemDetails.Rarity rarity, ItemType itemType, int turn);

}
