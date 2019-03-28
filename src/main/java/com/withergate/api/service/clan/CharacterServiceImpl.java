package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
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
import com.withergate.api.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Character service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class CharacterServiceImpl implements CharacterService {

    public static final int LEVEL_QUOCIENT = 10;

    private final CharacterRepository characterRepository;
    private final ClanService clanService;
    private final RandomService randomService;
    private final NameService nameService;
    private final TraitDetailsRepository traitDetailsRepository;
    private final NotificationService notificationService;

    public CharacterServiceImpl(CharacterRepository characterRepository, @Lazy ClanService clanService, RandomService randomService,
                                NameService nameService, TraitDetailsRepository traitDetailsRepository, NotificationService notificationService) {
        this.characterRepository = characterRepository;
        this.clanService = clanService;
        this.randomService = randomService;
        this.nameService = nameService;
        this.traitDetailsRepository = traitDetailsRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Character load(int characterId) {
        return characterRepository.getOne(characterId);
    }

    @Override
    public Character save(Character character) {
        return characterRepository.save(character);
    }

    @Transactional
    @Override
    public void deleteDeadCharacters() {
        for (Character character : characterRepository.findAll()) {
            if (character.getHitpoints() < 1) {
                log.debug("Deleting character {}.", character.getName());
                characterRepository.delete(character);
            }
        }
    }

    @Transactional
    @Override
    public void performCharacterTurnUpdates(int turnId) {
        // delete dead characters
        deleteDeadCharacters();

        // eat food
        performFoodConsumption(turnId);

        // perform character healing
        performCharacterHealing(turnId);

        // perform character leveling
        performCharacterLeveling(turnId);

        // mark characters as ready
        for (Character character : characterRepository.findAll()) {
            character.setState(CharacterState.READY);
            characterRepository.save(character);
        }
    }

    @Override
    public Character generateRandomCharacter() {
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
        character.setImageUrl(nameService.generateRandomAvatar(gender));

        /*
         * Set level.
         */
        character.setLevel(1);
        character.setExperience(0);

        return character;
    }

    private void performFoodConsumption(int turnId) {
        log.debug("Food consumption.");

        for (Clan clan : clanService.getAllClans()) {
            if (clan.getCharacters().size() < 1) continue;

            ClanNotification notification = new ClanNotification();
            notification.setHeader(clan.getName());
            notification.setTurnId(turnId);
            notification.setClanId(clan.getId());
            notification.setFoodIncome(0);
            notification.setInjury(0);
            notificationService.addLocalizedTexts(notification.getText(), "clan.foodConsumption", new String[]{});

            Iterator<Character> iterator = clan.getCharacters().iterator();
            while (iterator.hasNext()) {
                Character character = iterator.next();

                // ascetic
                if (character.getTraits().containsKey(TraitDetails.TraitName.ASCETIC)) {
                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.trait.ascetic", new String[]{character.getName()});
                    notification.getDetails().add(detail);

                    continue; // skip food consumption
                }

                if (clan.getFood() > 0) {
                    clan.setFood(clan.getFood() - 1);

                    NotificationDetail detail = new NotificationDetail();
                    notificationService.addLocalizedTexts(detail.getText(), "detail.character.foodConsumption", new String[]{character.getName()});
                    notification.getDetails().add(detail);
                    notification.setFoodIncome(notification.getFoodIncome() - 1);
                } else {
                    log.debug("Character {} is starving,", character.getName());

                    character.setHitpoints(character.getHitpoints() - 1);

                    NotificationDetail detail = new NotificationDetail();
                    notificationService
                            .addLocalizedTexts(detail.getText(), "detail.character.starving",
                                    new String[]{character.getName()});
                    notification.getDetails().add(detail);
                    notification.setInjury(notification.getInjury() + 1);

                    if (character.getHitpoints() < 1) {
                        log.debug("Character {} died of starvation.", character.getName());

                        NotificationDetail detailDeath = new NotificationDetail();
                        notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.starvationdeath",
                                new String[]{character.getName()});
                        notification.getDetails().add(detailDeath);

                        // delte and remove from clan
                        characterRepository.delete(character);
                        iterator.remove();
                    } else {
                        characterRepository.save(character);
                    }
                }
            }
            notificationService.save(notification);
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
            ClanNotification notification = new ClanNotification();
            notification.setTurnId(turnId);
            notification.setClanId(character.getClan().getId());
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
                            new String[]{String.valueOf(building.getLevel()), building.getDetails().getName()});
                    notification.getDetails().add(healingBuildingDetail);
                }
            }

            int healing = Math.min(points, hitpointsMissing);
            character.setHitpoints(character.getHitpoints() + healing);

            characterRepository.save(character);

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
                ClanNotification notification = new ClanNotification();
                notification.setTurnId(turnId);
                notification.setClanId(character.getClan().getId());
                notification.setHeader(character.getName());
                notificationService.addLocalizedTexts(notification.getText(), "character.levelup", new String[]{character.getName()});

                // add random trait to character
                Trait trait = getRandomTrait(character);
                character.getTraits().put(trait.getDetails().getIdentifier(), trait);
                log.debug("New trait assigned to {}: {}", character.getName(), trait.getDetails().getIdentifier());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.levelup.trait", new String[]{character.getName()});
                notification.getDetails().add(detail);

                // save
                characterRepository.save(character);
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
