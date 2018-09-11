package com.withergate.api.service.encounter;

import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.repository.EncounterRepository;
import com.withergate.api.service.IRandomService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.IItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Encounter service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class EncounterService implements IEncounterService {

    private final EncounterRepository encounterRepository;
    private final IItemService itemService;
    private final IRandomService randomService;
    private final ICombatService combatService;

    /**
     * Constructor.
     *
     * @param encounterRepository encounter repository
     * @param itemService item service
     * @param randomService  random service
     * @param combatService combat service
     */
    public EncounterService(EncounterRepository encounterRepository, IItemService itemService,
                            IRandomService randomService, ICombatService combatService) {
        this.encounterRepository = encounterRepository;
        this.itemService = itemService;
        this.randomService = randomService;
        this.combatService = combatService;
    }

    @Transactional
    @Override
    public void handleEncounter(ClanNotification notification, Character character, Location location) {
        // load random encounter from the repository
        List<Encounter> encounters = encounterRepository.findAll();
        int index = randomService.getRandomInt(0, encounters.size() - 1);
        Encounter encounter = encounters.get(index);

        log.debug("Processing {} with {} at {}", encounter.getType(), character.getName(), location.name());

        notification.setText(encounter.getDescriptionText(character, location));

        switch (encounter.getType()) {
            case COMBAT:
                // handle combat and check if character won, if yes, handle reward
                if (combatService.handleCombat(notification, encounter, character, location)) {
                    handleReward(encounter, character, notification);
                }
                break;
            case INTELLECT:
                int totalIntellect = character.getIntellect() + randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);
                log.debug("{} rolled dice and the total intellect value is {}", character.getName(), totalIntellect);
                if (totalIntellect < encounter.getDifficulty()) {
                    notification.setResult(encounter.getFailureText(character, location));
                } else {
                    handleReward(encounter, character, notification);
                    notification.setResult(encounter.getSuccessText(character, location));
                }
                break;
            default:
                log.error("Unknown encounter type triggered: {}!", encounter.getType());
                break;
        }
    }

    private void handleReward(Encounter encounter, Character character, ClanNotification notification) {
        log.debug("Computing reward for character {}", character.getId());

        switch (encounter.getReward()) {
            case CAPS:
                // TODO
                break;
            case JUNK:
                // TODO
                break;
            case ITEM:
                itemService.generateItemForCharacter(character, notification);
                break;
            case CHARACTER:
                // TODO
                break;
            default:
                // TODO
                break;
        }
    }

}
