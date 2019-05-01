package com.withergate.api.model.request;

import com.withergate.api.model.item.ItemType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WeaponEquip request. Used when equipping a weapon..
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EquipRequest {

    private int itemId;
    private int characterId;
    private ItemType itemType;
}
