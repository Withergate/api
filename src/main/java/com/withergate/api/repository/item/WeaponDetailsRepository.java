package com.withergate.api.repository.item;

import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.WeaponDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * WeaponDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface WeaponDetailsRepository extends JpaRepository<WeaponDetails, String> {

    List<WeaponDetails> findAllByRarity(ItemDetails.Rarity rarity);

    List<WeaponDetails> findAllByRarityAndCraftable(ItemDetails.Rarity rarity, boolean craftable);

}
