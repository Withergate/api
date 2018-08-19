package com.withergate.api.service.encounter;

import com.withergate.api.model.Location;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.repository.EncounterRepository;
import com.withergate.api.service.RandomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final RandomService randomService;

    /**
     * Constructor.
     *
     * @param encounterRepository encounter repository
     */
    public EncounterService(EncounterRepository encounterRepository, RandomService randomService) {
        this.encounterRepository = encounterRepository;
        this.randomService = randomService;
    }

    @Override
    public void handleEncounter(ClanNotification notification, Character character, Location location) {
        // load random encounter from the repository
        List<Encounter> encounters = encounterRepository.findAll();
        int index = randomService.getRandomInt(0, encounters.size() - 1);
        Encounter encounter = encounters.get(index);

        log.debug("Processing {} with {} at {}", encounter.getType(), character.getName(), location.name());

        StringBuilder notificationBuilder = new StringBuilder();
        notificationBuilder.append(encounter.getDescriptionText(character, location));

        switch (encounter.getType()) {
            case COMBAT:
                int totalCombat = character.getTotalCombat() + randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);
                log.debug("{} rolled dice and the total combat value is {}", character.getName(), totalCombat);
                if (totalCombat < encounter.getDifficulty()) {
                    character.setState(CharacterState.INJURED);
                    notificationBuilder.append(" ");
                    notificationBuilder.append(encounter.getFailureText(character, location));
                } else {
                    // TODO - compute reward
                    notificationBuilder.append(" ");
                    notificationBuilder.append(encounter.getSuccessText(character, location));
                }
                break;
            case CHARM:
                int totalCharm = character.getCharm() + randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);
                log.debug("{} rolled dice and the total charm value is {}", character.getName(), totalCharm);
                if (totalCharm < encounter.getDifficulty()) {
                    notificationBuilder.append(" ");
                    notificationBuilder.append(encounter.getFailureText(character, location));
                } else {
                    // TODO - compute reward
                    notificationBuilder.append(" ");
                    notificationBuilder.append(encounter.getSuccessText(character, location));
                }
                break;
            case INTELLECT:
                int totalIntellect = character.getCharm() + randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE);
                log.debug("{} rolled dice and the total intellect value is {}", character.getName(), totalIntellect);
                if (totalIntellect < encounter.getDifficulty()) {
                    notificationBuilder.append(" ");
                    notificationBuilder.append(encounter.getFailureText(character, location));
                } else {
                    // TODO - compute reward
                    notificationBuilder.append(" ");
                    notificationBuilder.append(encounter.getSuccessText(character, location));
                }
                break;
            default:
                log.error("Unknown encounter type triggered: {}!", encounter.getType());
                break;
            }

             // Update notification
             notification.setText(notificationBuilder.toString());
    }

}
