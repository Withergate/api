package com.withergate.api.service.combat;

import com.withergate.api.model.Clan;
import com.withergate.api.model.EndBonusType;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.item.Item;
import com.withergate.api.model.item.ItemDetails;
import com.withergate.api.model.item.ItemType;

/**
 * Utils class used for computing default clan defense.
 *
 * @author Martin Myslik
 */
public class ClanDefenseUtils {

    private static final int BASE_COMBAT = 2;
    private static final int BASE_HEALTH = 10;
    private static final int COMBAT_PER_LEVEL = 2;
    private static final int HEALTH_PER_LEVEL = 5;

    private ClanDefenseUtils() {
        // disabled
    }

    /**
     * Prepares default defender for provided clan.
     *
     * @param clan clan
     * @return defender
     */
    public static Character getDefender(Clan clan) {
        Building building = clan.getBuildings().stream().filter(b ->
                b.getDetails().getEndBonusType() != null
                        && b.getDetails().getEndBonusType().equals(EndBonusType.CLAN_DEFENSE))
                .findFirst().orElse(null);

        int buildingLevel = building != null ? building.getLevel() : 0;

        Character character = new Character();
        character.setName("Doggo");
        character.setCombat(BASE_COMBAT + COMBAT_PER_LEVEL * buildingLevel);
        character.setMaxHitpoints(BASE_HEALTH + HEALTH_PER_LEVEL * buildingLevel);
        character.setHitpoints(character.getMaxHitpoints());

        ItemDetails fur = new ItemDetails();
        fur.setCombat(buildingLevel / 2);
        fur.setItemType(ItemType.OUTFIT);
        Item outfit = new Item();
        outfit.setDetails(fur);
        character.getItems().add(outfit);

        return character;
    }

}
