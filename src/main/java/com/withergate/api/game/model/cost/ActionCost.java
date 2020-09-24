package com.withergate.api.game.model.cost;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.item.ItemCost;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.utils.ResourceUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Action cost entity.
 *
 * @author Martin Myslik
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ActionCost {

    @Column(name = "food_cost", nullable = false)
    private int foodCost;

    @Column(name = "junk_cost", nullable = false)
    private int junkCost;

    @Column(name = "caps_cost", nullable = false)
    private int capsCost;

    @Column(name = "information_cost", nullable = false)
    private int informationCost;

    @Column(name = "faction_points_cost", nullable = false)
    private int factionPointsCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_cost", updatable = false, nullable = false)
    private ItemCost itemCost;

    @Column(name = "health_cost", nullable = false)
    private boolean healthCost;

    /**
     * Pays resources for the action.
     *
     * @param clan clan
     * @throws InvalidActionException if insufficient resources
     */
    public void payResources(Clan clan) throws InvalidActionException {
        // check resources
        if (clan.getCaps() < capsCost || clan.getFood() < foodCost || clan.getJunk() < junkCost) {
            throw new InvalidActionException("error.no-resources", "Not enough resources.");
        }
        if (informationCost > 0 && clan.getInformation() < informationCost) {
            throw new InvalidActionException("error.no-information", "Not enough information.");
        }
        if (factionPointsCost > 0 && clan.getFactionPoints() < factionPointsCost) {
            throw new InvalidActionException("error.no-influence", "Not enough influence.");
        }

        // pay resources
        ResourceUtils.changeCaps(- capsCost, clan, null);
        ResourceUtils.changeFood(- foodCost, clan, null);
        ResourceUtils.changeJunk(- junkCost, clan, null);
        ResourceUtils.changeInformation(- informationCost, clan, null);
        ResourceUtils.changeFactionPoints(- factionPointsCost, clan, null);
    }

    /**
     * Refunds resources for the action.
     *
     * @param clan clan
     */
    public void refundResources(Clan clan) {
        ResourceUtils.changeCaps(capsCost, clan, null);
        ResourceUtils.changeFood(foodCost, clan, null);
        ResourceUtils.changeJunk(junkCost, clan, null);
        ResourceUtils.changeInformation(informationCost, clan, null);
        ResourceUtils.changeFactionPoints(factionPointsCost, clan, null);
    }

}
