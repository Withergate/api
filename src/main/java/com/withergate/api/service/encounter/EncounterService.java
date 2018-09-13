package com.withergate.api.service.encounter;

import com.withergate.api.model.Clan;
import com.withergate.api.model.ClanNotification;
import com.withergate.api.model.Location;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.encounter.Encounter;
import com.withergate.api.repository.EncounterRepository;
import com.withergate.api.service.IRandomService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.ICharacterService;
import com.withergate.api.service.clan.IClanService;
import com.withergate.api.service.item.IItemService;
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
    private final IClanService clanService;
    private final ICharacterService characterService;

    /**
     * Constructor.
     *
     * @param encounterRepository encounter repository
     * @param itemService item service
     * @param randomService  random service
     * @param combatService combat service
     * @param clanService clan service
     * @param characterService  character service
     */
    public EncounterService(EncounterRepository encounterRepository, IItemService itemService,
                            IRandomService randomService, ICombatService combatService, IClanService clanService,
                            ICharacterService characterService) {
        this.encounterRepository = encounterRepository;
        this.itemService = itemService;
        this.randomService = randomService;
        this.combatService = combatService;
        this.clanService = clanService;
        this.characterService = characterService;
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

        Clan clan = character.getClan();

        switch (encounter.getReward()) {
            case CAPS:
                // add caps
                int caps = randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE) * 2; // random amount of caps

                clan.setCaps(clan.getCaps() + caps);
                clanService.saveClan(clan);

                // update notification
                notification.setIncome("Added [" + caps + "] caps to your clan storage.");
                break;
            case JUNK:
                // add junk
                int junk = randomService.getRandomInt(1, RandomService.ENCOUNTER_DICE) * 2; // random amount of caps
                clan.setJunk(clan.getJunk() + junk);
                clanService.saveClan(clan);

                // update notification
                notification.setIncome("Added [" + junk + "] junk to your clan storage.");
                break;
            case ITEM:
                // generate item
                itemService.generateItemForCharacter(character, notification);
                break;
            case CHARACTER:
                // generate character
                Character generated = characterService.generateRandomCharacter();
                generated.setClan(clan);
                clan.getCharacters().add(generated);
                clanService.saveClan(clan);

                // update notification
                notification.setIncome("[" + generated.getName() + "] joined your clan.");
                break;
            default:
                log.error("Unknown type of reward!");
                break;
        }
    }

}
