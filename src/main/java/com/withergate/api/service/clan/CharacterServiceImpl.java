package com.withergate.api.service.clan;

import java.util.List;
import java.util.stream.Collectors;

import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.TraitDetailsRepository;
import com.withergate.api.service.NameService;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Character service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class CharacterServiceImpl implements CharacterService {

    public static final int LEVEL_QUOTIENT = 10;
    public static final int FREE_TRAIT_THRESHOLD = 10;

    private final CharacterRepository characterRepository;
    private final RandomService randomService;
    private final NameService nameService;
    private final TraitDetailsRepository traitDetailsRepository;
    private final NotificationService notificationService;

    @Override
    public Character load(int characterId) {
        return characterRepository.getOne(characterId);
    }

    @Override
    public Character save(Character character) {
        return characterRepository.save(character);
    }

    @Override
    public void delete(Character character) {
        characterRepository.delete(character);
    }

    @Transactional
    @Override
    public void performCharacterTurnUpdates(int turnId) {
        // delete dead characters
        deleteDeadCharacters();

        // handle resting characters
        markRestingCharactersReady();

        // perform character healing
        performCharacterHealing(turnId);

        // perform character leveling
        performCharacterLeveling(turnId);

        // mark characters as ready
        for (Character character : characterRepository.findAll()) {
            character.setState(CharacterState.READY);
        }
    }

    @Override
    public Character generateRandomCharacter(CharacterFilter filter) {
        Character character = new Character();

        /*
         * Set character's state to READY.
         */
        character.setState(CharacterState.READY);

        /*
         * Generate random gender.
         */
        Gender gender = randomService.getRandomGender();
        character.setGender(gender);

        /*
         * Generate random name.
         */
        String name = nameService.generateRandomName(gender);
        character.setName(name);

        /*
         * Generate random hitpoints.
         */
        int hitpoints = RandomServiceImpl.K10 + randomService.getRandomInt(1, RandomServiceImpl.K10);
        character.setHitpoints(hitpoints);
        character.setMaxHitpoints(hitpoints);

        /*
         * Generate random stats.
         */
        character.setCombat(randomService.getRandomInt(1, 5));
        character.setScavenge(randomService.getRandomInt(1, 5));
        character.setCraftsmanship(randomService.getRandomInt(1, 5));
        character.setIntellect(randomService.getRandomInt(1, 5));

        /*
         * Generate random avatar.
         */
        character.setImageUrl(nameService.generateRandomAvatar(gender, filter.getAvatars()));

        /*
         * Set level.
         */
        character.setLevel(1);
        character.setExperience(0);

        /*
         * Add random trait to weak characters.
         */
        if ((character.getCombat() + character.getScavenge() + character.getCraftsmanship()
                + character.getIntellect()) <= FREE_TRAIT_THRESHOLD) {
            Trait trait = getRandomTrait(character);
            character.getTraits().put(trait.getDetails().getIdentifier(), trait);
        }

        return character;
    }

    @Override
    public void markCharacterAsResting(int characterId, int clanId) throws InvalidActionException {
        // check action validity
        Character character = load(characterId);
        if (character == null || character.getClan().getId() != clanId) {
            throw new InvalidActionException("This character either doesn't exist or doesn't belong to your clan.");
        }

        if (!character.getState().equals(CharacterState.READY)) {
            throw new InvalidActionException("Character must be ready to perform this action.");
        }

        character.setState(CharacterState.RESTING);
        save(character);
    }

    private void deleteDeadCharacters() {
        for (Character character : characterRepository.findAll()) {
            if (character.getHitpoints() < 1) {
                log.debug("Deleting character {}.", character.getName());
                character.getClan().getCharacters().remove(character);

                characterRepository.delete(character);
            }
        }
    }

    private void markRestingCharactersReady() {
        for (Character character : characterRepository.findAllByState(CharacterState.RESTING)) {
            character.setState(CharacterState.READY);
        }
    }

    private void performCharacterHealing(int turnId) {
        log.debug("Performing healing action");

        // load all characters that are ready
        List<Character> characters = characterRepository.findAllByState(CharacterState.READY);

        for (Character character : characters) {
            // skip if there is no food
            if (character.getClan().getFood() < 1) {
                continue;
            }

            int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();

            if (hitpointsMissing == 0) {
                continue;
            }

            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
            notification.setHeader(character.getName());

            // each character that is ready heals
            int points = randomService.getRandomInt(1, 2);
            NotificationDetail healingRollDetail = new NotificationDetail();
            notificationService.addLocalizedTexts(healingRollDetail.getText(), "detail.healing.roll",
                    new String[]{String.valueOf(points)});
            notification.getDetails().add(healingRollDetail);
            if (character.getClan().getBuildings().containsKey(BuildingDetails.BuildingName.SICK_BAY)) {
                Building building = character.getClan().getBuildings().get(BuildingDetails.BuildingName.SICK_BAY);
                points += building.getLevel();

                if (building.getLevel() > 0) {
                    NotificationDetail healingBuildingDetail = new NotificationDetail();
                    notificationService.addLocalizedTexts(healingBuildingDetail.getText(), "detail.healing.building",
                            new String[]{String.valueOf(building.getLevel())});
                    notification.getDetails().add(healingBuildingDetail);
                }
            }

            int healing = Math.min(points, hitpointsMissing);
            character.setHitpoints(character.getHitpoints() + healing);

            notificationService
                    .addLocalizedTexts(notification.getText(), "character.healing", new String[]{});
            notification.setHealing(points);

            notificationService.save(notification);
        }
    }

    private void performCharacterLeveling(int turnId) {
        log.debug("Performing character leveling action");

        // load all characters
        List<Character> characters = characterRepository.findAll();

        for (Character character : characters) {
            if (character.getExperience() >= character.getNextLevelExperience()) {
                // level up
                log.debug("Character {} leveled up.", character.getName());

                character.setExperience(character.getExperience() - character.getNextLevelExperience());
                int hpIncrease = randomService.getRandomInt(1, RandomServiceImpl.K6);
                character.setMaxHitpoints(character.getMaxHitpoints() + hpIncrease);
                character.setHitpoints(character.getHitpoints() + hpIncrease);
                character.setLevel(character.getLevel() + 1);

                // notification
                ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
                notification.setHeader(character.getName());
                notificationService.addLocalizedTexts(notification.getText(), "character.levelup", new String[]{character.getName()});

                // add random trait to character
                Trait trait = getRandomTrait(character);
                character.getTraits().put(trait.getDetails().getIdentifier(), trait);
                log.debug("New trait assigned to {}: {}", character.getName(), trait.getDetails().getIdentifier());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.levelup.trait",
                        new String[]{character.getName()});
                notification.getDetails().add(detail);

                // save
                notificationService.save(notification);
            }
        }
    }

    private Trait getRandomTrait(Character character) {
        List<TraitDetails> detailsList = traitDetailsRepository.findAll().stream()
                .filter(trait -> !character.getTraits().containsKey(trait.getIdentifier())).collect(
                        Collectors.toList());
        TraitDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));

        Trait trait = new Trait();
        trait.setDetails(details);

        return trait;
    }

}
