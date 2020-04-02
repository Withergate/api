package com.withergate.api.service.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemDetails.Rarity;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.repository.item.ItemDetailsRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Crafting service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CraftingServiceImpl implements CraftingService {

    private final ClanService clanService;
    private final ItemDetailsRepository detailsRepository;

    @Override
    public List<ItemDetails> getAvailableItems(int clanId) {
        // load clan
        Clan clan = clanService.getClan(clanId);

        List<ItemDetails> detailsList = detailsRepository.findAll().stream()
                .filter(details -> !details.getRarity().equals(Rarity.EPIC)).collect(
                Collectors.toList());
        List<ItemDetails> resultList = new ArrayList<>();

        for (ItemDetails details : detailsList) {
            try {
                if (checkCraftingRequirements(details, clan)) {
                    resultList.add(details);
                }
            } catch (InvalidActionException e) {
                log.error("Cannot check crafting requirements for item.", e);
            }
        }

        return resultList;
    }

    @Override
    public void processCraftingActions(int turnId) {
        // TODO
    }

    private boolean checkCraftingRequirements(ItemDetails details, Clan clan) throws InvalidActionException {
        Building building = getBuildingForItem(details.getItemType(), clan);

        if (building.getLevel() >= details.getCraftingLevel()) {
            return true;
        }

        return false;
    }

    private Building getBuildingForItem(ItemType type, Clan clan) throws InvalidActionException {
        for (Building building : clan.getBuildings()) {
            if (building.getDetails().getPassiveBonusType() == null) continue;
            if (type.equals(ItemType.WEAPON)
                    && building.getDetails().getPassiveBonusType().equals(PassiveBonusType.CRAFTING_WEAPON)) {
                return building;
            } else if (type.equals(ItemType.GEAR)
                    && building.getDetails().getPassiveBonusType().equals(PassiveBonusType.CRAFTING_GEAR)) {
                return building;
            } else if (type.equals(ItemType.OUTFIT)
                    && building.getDetails().getPassiveBonusType().equals(PassiveBonusType.CRAFTING_OUTFIT)) {
                return building;
            }
        }

        throw new InvalidActionException("No crafting building found for item type " + type.name());
    }
}
