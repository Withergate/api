package com.withergate.api.service.location;

import java.util.List;

import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.LocationAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.item.Gear;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.location.LocationDescription;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.LocationDescriptionRepository;
import com.withergate.api.repository.action.LocationActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.encounter.EncounterService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Location service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationActionRepository locationActionRepository;
    private final RandomService randomService;
    private final EncounterService encounterService;
    private final ItemService itemService;
    private final NotificationService notificationService;
    private final LocationDescriptionRepository locationDescriptionRepository;

    @Override
    public LocationDescription getLocationDescription(Location location) {
        return locationDescriptionRepository.getOne(location);
    }

    @Override
    public void saveLocationAction(LocationAction action) {
        locationActionRepository.save(action);
    }

    @Override
    public void processLocationActions(int turnId) {
        log.info("Executing location actions...");
        List<LocationAction> actions = locationActionRepository.findAllByState(ActionState.PENDING);

        for (LocationAction action : actions) {
            Character character = action.getCharacter();

            log.debug("Character {} is exploring {}.", character.getName(), action.getLocation());

            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
            notification.setHeader(character.getName());
            notification.setImageUrl(character.getImageUrl());

            // process action
            processLocationAction(notification, action);

            // send notification about action result
            notificationService.save(notification);

            action.setState(ActionState.COMPLETED);
        }
    }

    private void processLocationAction(ClanNotification notification, LocationAction action) {
        Character character = action.getCharacter();
        LocationDescription description = locationDescriptionRepository.getOne(action.getLocation());

        /*
         * ENCOUNTER
         *
         * If the character encounters a random event, process it and finish exploration. The chance for an encounter
         * differs based on location type.
         *
         * Character's intellect can decrease encounter chance.
         */
        int encounterRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (encounterRoll <= description.getEncounterChance() - character.getIntellect()) {
            log.debug("Random encounter triggered!");

            encounterService.handleEncounter(notification, character, action.getLocation());
            return;
        }

        // add experience for exploration
        character.setExperience(character.getExperience() + 1);
        notification.setExperience(1);

        /*
         * INFORMATION
         */
        if (action.getType() == LocationAction.LocationActionType.SCOUT) {
            log.debug("Information increased.");
            int information = action.getCharacter().getIntellect() + description.getInformationBonus();

            Clan clan = character.getClan();
            clan.setInformation(clan.getInformation() + information);

            notificationService.addLocalizedTexts(notification.getText(), "location.information", new String[] {});
            notification.setInformation(information);

            return;
        }

        /*
         * LOOT
         */
        int lootProbability = description.getItemChance() + character.getScavenge();
        int lootRoll = randomService.getRandomInt(1, RandomServiceImpl.K100);
        if (lootRoll <= lootProbability) {
            notificationService.addLocalizedTexts(notification.getText(), "location.loot", new String[] {});
            itemService.generateItemForCharacter(character, notification);
        }

        // Junk and food
        Clan clan = character.getClan();
        notificationService.addLocalizedTexts(notification.getText(), "location.resources", new String[] {});

        int junk = character.getScavenge() + description.getJunkBonus() + getIncomeBonus(character, notification,
                BonusType.SCAVENGE_JUNK);
        clan.setJunk(clan.getJunk() + junk);
        notification.setJunkIncome(junk);

        int food = character.getScavenge() + description.getFoodBonus() + getIncomeBonus(character, notification,
                BonusType.SCAVENGE_FOOD);
        clan.setFood(clan.getFood() + food);
        notification.setFoodIncome(food);
    }

    // add bonus to found junk and food when character has certain traits or items
    private int getIncomeBonus(Character character, ClanNotification notification, BonusType bonusType) {
        int bonus = 0;

        if (bonusType.equals(BonusType.SCAVENGE_FOOD) && character.getTraits().containsKey(TraitName.HUNTER)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.trait.scavenge", new String[]{},
                    character.getTraits().get(TraitName.HUNTER).getDetails().getName());
            notification.getDetails().add(detail);

            bonus += character.getTraits().get(TraitName.HUNTER).getDetails().getBonus();
        }

        if (bonusType.equals(BonusType.SCAVENGE_JUNK) && character.getTraits().containsKey(TraitName.HOARDER)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.trait.scavenge", new String[]{},
                    character.getTraits().get(TraitName.HOARDER).getDetails().getName());
            notification.getDetails().add(detail);

            bonus += character.getTraits().get(TraitName.HOARDER).getDetails().getBonus();
        }

        Gear gear = character.getGear();
        if (bonusType.equals(BonusType.SCAVENGE_JUNK) && gear != null && gear.getDetails().getBonusType().equals(BonusType.SCAVENGE_JUNK)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "gear.bonus.junk", new String[] {}, gear.getDetails().getName());
            notification.getDetails().add(detail);

            bonus += character.getGear().getDetails().getBonus();
        }

        if (bonusType.equals(BonusType.SCAVENGE_FOOD) && gear != null && gear.getDetails().getBonusType().equals(BonusType.SCAVENGE_FOOD)) {
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "gear.bonus.food", new String[] {}, gear.getDetails().getName());
            notification.getDetails().add(detail);

            bonus += character.getGear().getDetails().getBonus();
        }

        return bonus;
    }
}
