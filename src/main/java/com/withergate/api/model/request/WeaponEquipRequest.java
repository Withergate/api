package com.withergate.api.model.request;

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
public class WeaponEquipRequest {

    private int weaponId;
    private int characterId;
}
