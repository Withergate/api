package com.withergate.api.service.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.CraftingAction;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemDetails.Rarity;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.CraftingRequest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.repository.action.CraftingActionRepository;
import com.withergate.api.game.repository.item.ItemDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CharacterService characterService;
    private final ItemDetailsRepository detailsRepository;
    private final CraftingActionRepository actionRepository;
    private final ItemService itemService;
    private final NotificationService notificationService;
    private final RandomService randomService;

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

    @Transactional
    @Override
    public void saveCraftingAction(CraftingRequest request, int clanId) throws InvalidActionException {
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();

        // load details
        ItemDetails details = detailsRepository.getOne(request.getItem());

        // check prereq
        if (!checkCraftingRequirements(details, clan)) {
            throw new InvalidActionException("This item is not available to you.");
        }

        // check cost
        int cost = details.getCraftingCost() - character.getCraftsmanship();
        if (clan.getJunk() < cost) {
            throw new InvalidActionException("Not enough junk to perform this action.");
        }

        // pay cost
        clan.changeJunk(- cost);

        CraftingAction action = new CraftingAction();
        action.setCraftingItem(details.getIdentifier());
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        actionRepository.save(action);

        // mark character as busy
        character.setState(CharacterState.BUSY);
    }

    @Override
    public void processCraftingActions(int turnId) {
        for (CraftingAction action : actionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setImageUrl(action.getCharacter().getImageUrl());
            notification.setHeader(action.getCharacter().getName());

            // handle action
            handleCraftingAction(action, notification);

            // save notification and update action state
            notificationService.save(notification);
            action.setState(ActionState.COMPLETED);
        }
    }

    private void handleCraftingAction(CraftingAction action, ClanNotification notification) {
        ItemDetails details = detailsRepository.getOne(action.getCraftingItem());

        // craft item
        itemService.generateCraftableItem(action.getCharacter(), notification, details);

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), "crafting.item", new String[]{});

        // handle crafting research bonuses
        Clan clan = action.getCharacter().getClan();
        Research researchCaps = clan.getResearch(ResearchBonusType.CRAFTING_CAPS);
        if (researchCaps.isCompleted()) {
            int caps = randomService.getRandomInt(1, RandomServiceImpl.K4);
            clan.changeCaps(caps);
            notification.changeCaps(caps);

            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), researchCaps.getDetails().getBonusText(), new String[]{});
            notification.getDetails().add(detail);
        }

        Research researchFame = clan.getResearch(ResearchBonusType.CRAFTING_FAME);
        if (researchFame.isCompleted()) {
            // check cost
            if (clan.getJunk() >= researchFame.getDetails().getCostAction()) {
                clan.changeJunk(- researchFame.getDetails().getCostAction());
                notification.changeJunk(- researchFame.getDetails().getCostAction());

                clan.changeFame(researchFame.getDetails().getValue());
                notification.changeFame(researchFame.getDetails().getValue());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), researchFame.getDetails().getBonusText(), new String[]{});
                notification.getDetails().add(detail);
            }
        }
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
