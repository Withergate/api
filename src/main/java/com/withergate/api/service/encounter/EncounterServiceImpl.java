package com.withergate.api.service.encounter;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.model.location.Location;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.EncounterRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.combat.CombatService;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Encounter service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;
    private final ItemService itemService;
    private final RandomService randomService;
    private final CombatService combatService;
    private final ClanService clanService;
    private final CharacterService characterService;
    private final NotificationService notificationService;

    @Override
    public void handleEncounter(ClanNotification notification, Character character, Location location) {
        // load random encounter from the repository
        List<Encounter> encounters = encounterRepository.findAllByLocation(location);
        int index = randomService.getRandomInt(0, encounters.size() - 1);
        Encounter encounter = encounters.get(index);

        log.debug("Processing {} with {} at {}", encounter.getType(), character.getName(), location.name());

        notificationService.addLocalizedTexts(notification.getText(), encounter.getDescriptionText(), new String[]{});

        switch (encounter.getType()) {
            case COMBAT:
                // handle combat and check if character won, if yes, handle reward
                if (combatService.handleSingleCombat(notification, encounter.getDifficulty(), character)) {
                    handleSuccess(encounter, character, notification);
                } else {
                    handleFailure(encounter, character, notification);
                }
                break;
            case INTELLECT:
                int totalIntellect = character.getIntellect() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total intellect value is {}", character.getName(), totalIntellect);
                if (totalIntellect < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                }
                break;
            case SCAVENGE:
                int totalScavenge = character.getScavenge() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total scavenge value is {}", character.getName(), totalScavenge);
                if (totalScavenge < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                }
                break;
            case CRAFTSMANSHIP:
                int totalCraftsmanship = character.getCraftsmanship() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total craftsmanship value is {}", character.getName(), totalCraftsmanship);
                if (totalCraftsmanship < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                }
                break;
            case COMBAT_ROLL:
                int totalCombat = character.getCombat() + randomService.getRandomInt(1, RandomServiceImpl.K6);
                log.debug("{} rolled dice and the total combat value is {}", character.getName(), totalCombat);
                if (totalCombat < encounter.getDifficulty()) {
                    handleFailure(encounter, character, notification);
                } else {
                    handleSuccess(encounter, character, notification);
                }
                break;
            default:
                log.error("Unknown encounter type triggered: {}!", encounter.getType());
                break;
        }
    }

    private void handleSuccess(Encounter encounter, Character character, ClanNotification notification) {
        log.debug("Computing reward for character {}", character.getId());

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), encounter.getSuccessText(), new String[]{});

        // handle experience
        character.setExperience(character.getExperience() + 2);
        notification.setExperience(2);

        Clan clan = character.getClan();

        switch (encounter.getReward()) {
            case CAPS:
                // add caps
                int caps = randomService.getRandomInt(1, RandomServiceImpl.K6) * 2; // random amount of caps

                clan.setCaps(clan.getCaps() + caps);
                clanService.saveClan(clan);

                notification.setCapsIncome(caps);
                break;
            case JUNK:
                // add junk
                int junk = randomService.getRandomInt(1, RandomServiceImpl.K6) * 2; // random amount of caps
                clan.setJunk(clan.getJunk() + junk);
                clanService.saveClan(clan);

                notification.setJunkIncome(junk);
                break;
            case INFORMATION:
                // add information
                int information = randomService.getRandomInt(1, RandomServiceImpl.K6) * 2; // random amount of information
                clan.setInformation(clan.getInformation() + information);
                clanService.saveClan(clan);

                notification.setInformation(information);
                break;
            case ITEM:
                // generate item
                itemService.generateItemForCharacter(character, notification);
                break;
            case CHARACTER:
                // generate character
                Character generated = characterService.generateRandomCharacter(prepareCharacterFilter(clan));
                generated.setClan(clan);
                clan.getCharacters().add(generated);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.joined", new String[]{character.getName()});
                notification.getDetails().add(detail);
                break;
            default:
                log.error("Unknown type of reward!");
                break;
        }
    }

    private void handleFailure(Encounter encounter, Character character, ClanNotification notification) {
        log.debug("Computing penalty for character {}", character.getId());

        // update notification
        notificationService.addLocalizedTexts(notification.getText(), encounter.getFailureText(), new String[]{});

        // handle experience
        character.setExperience(character.getExperience() + 1);
        notification.setExperience(1);

        Clan clan = character.getClan();

        switch (encounter.getPenalty()) {
            case NONE:
                break;
            case CAPS:
                // add caps
                int diceRoll = randomService.getRandomInt(1, RandomServiceImpl.K6) * 2; // random amount of caps
                int caps = Math.min(clan.getCaps(), diceRoll);

                clan.setCaps(clan.getCaps() - caps);

                // update notification
                notification.setCapsIncome(- caps);
                break;
            case INJURY:
                int injury = randomService.getRandomInt(1, RandomServiceImpl.K6);

                character.setHitpoints(character.getHitpoints() - injury);
                notification.setInjury(injury);

                if (character.getHitpoints() < 1) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.character.injurydeath",
                            new String[]{character.getName()});
                    notification.getDetails().add(detail);
                    notification.setDeath(true);
                }
                break;
            default:
                log.error("Unknown type of penalty: {}!", encounter.getPenalty());
                break;
        }
    }

    private CharacterFilter prepareCharacterFilter(Clan clan) {
        CharacterFilter filter = new CharacterFilter();
        for (Character character : clan.getCharacters()) {
            filter.getAvatars().add(character.getImageUrl());
            filter.getNames().add(character.getName());
        }

        return filter;
    }

}
