package com.withergate.api.repository.item;

import java.util.List;

import com.withergate.api.model.item.ConsumableDetails;
import com.withergate.api.model.item.GearDetails;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.OutfitDetails;
import com.withergate.api.model.item.WeaponDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ItemDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetails, String> {

    List<WeaponDetails> findWeaponDetailsByRarity(ItemDetails.Rarity rarity);
    List<WeaponDetails> findWeaponDetailsByRarityAndCraftable(ItemDetails.Rarity rarity, boolean craftable);

    List<OutfitDetails> findOutfitDetailsByRarity(ItemDetails.Rarity rarity);
    List<OutfitDetails> findOutfitDetailsByRarityAndCraftable(ItemDetails.Rarity rarity, boolean craftable);

    List<GearDetails> findGearDetailsByRarity(ItemDetails.Rarity rarity);
    List<GearDetails> findGearDetailsByRarityAndCraftable(ItemDetails.Rarity rarity, boolean craftable);

    List<ConsumableDetails> findConsumableDetailsByRarity(ItemDetails.Rarity rarity);

}
