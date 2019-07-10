package com.withergate.api.service.clan;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Clan service. Handles all basic operations over the clan entity.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class ClanServiceImpl implements ClanService {

    public static final int INFORMATION_QUOTIENT = 10;
    public static final int BASIC_POPULATION_LIMIT = 6;
    public static final int INITIAL_CLAN_SIZE = 5;
    public static final int HEALING = 2;
    public static final int FOOD_CONSUMPTION = 2;

    private static final int CLAN_NAME_LENGTH = 6;

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final NotificationService notificationService;
    private final QuestService questService;
    private final BuildingService buildingService;
    private final TavernService tavernService;
    private final RandomService randomService;
    private final TraitService traitService;

    @Override
    public Clan getClan(int clanId) {
        return clanRepository.findById(clanId).orElse(null);
    }

    @Override
    public List<Clan> getAllClans() {
        return clanRepository.findAll();
    }

    @Override
    public Page<Clan> getClans(Pageable pageable) {
        return clanRepository.findAll(pageable);
    }

    @Override
    public Clan saveClan(Clan clan) {
        return clanRepository.save(clan);
    }

    @Transactional
    @Override
    public Clan createClan(int clanId, ClanRequest clanRequest) throws EntityConflictException, ValidationException {
        // validate clan name
        if (!clanRequest.getName().matches("[a-zA-Z]+") || clanRequest.getName().length() < CLAN_NAME_LENGTH) {
            throw new ValidationException("Clan name must be at least 6 characters long and consist only of English letters.");
        }

        // check if clan already exists.
        if (getClan(clanId) != null) {
            log.warn("Cannot create a clan with ID {}", clanId);
            throw new EntityConflictException("Clan with the provided ID already exists.");
        }

        // check for name collisions. Clan name must be unique among all players.
        if (clanRepository.findOneByName(clanRequest.getName()) != null) {
            log.warn("Cannot create a clan with name {}", clanRequest.getName());
            throw new EntityConflictException("Clan with the provided name already exists.");
        }

        // Create clan with initial resources.
        Clan clan = new Clan();
        clan.setId(clanId);
        clan.setName(clanRequest.getName());
        clan.setLastActivity(LocalDateTime.now());
        clan.setFame(0);
        clan.setCaps(50);
        clan.setJunk(20);
        clan.setFood(20);
        clan.setInformation(0);
        clan.setInformationLevel(0);
        clan.setCharacters(new HashSet<>());
        clan.setDefaultAction(Clan.DefaultAction.REST);

        // assign random initial characters to clan.
        CharacterFilter filter = new CharacterFilter();
        for (int i = 0; i < INITIAL_CLAN_SIZE; i++) {
            Character character = characterService.generateRandomCharacter(filter);
            character.setClan(clan);
            clan.getCharacters().add(character);

            // filter out used avatars
            filter.getAvatars().add(character.getImageUrl());
            filter.getNames().add(character.getName());
        }

        // set buildings
        clan.setBuildings(new HashMap<>());

        for (BuildingDetails details : buildingService.getAllBuildingDetails()) {
            Building building = new Building();
            building.setProgress(0);
            building.setLevel(0);
            building.setDetails(details);
            building.setClan(clan);
            clan.getBuildings().put(details.getIdentifier(), building);
        }

        clan = clanRepository.save(clan);

        // prepare tavern offers
        tavernService.prepareTavernOffers(clan, filter);

        return clan;
    }

    @Override
    public Character hireCharacter(Clan clan) {
        log.debug("Hiring new character for clan {}", clan.getId());

        // create a random character
        Character character = characterService.generateRandomCharacter(getCharacterFilter(clan));
        character.setClan(clan);

        // deduct price and add the character to the clan
        clan.getCharacters().add(character);

        log.debug("Hired new character: {}", character.getName());
        return character;
    }

    @Override
    public void increaseInformationLevel(Clan clan, ClanNotification notification, int informationLevel) {
        log.debug("Increasing clan's information level for clan: {}", clan.getName());

        // handle level up
        clan.setInformationLevel(informationLevel);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.information.levelup", new String[] {});
        notification.getDetails().add(detail);

        // assign quests
        questService.assignQuests(clan, notification, informationLevel);
    }

    @Transactional
    @Override
    public void performClanTurnUpdates(int turnId) {
        for (Clan clan : getAllClans()) {
            // delete dead characters
            deleteDeadCharacters(clan);

            // passive buildings
            buildingService.processPassiveBuildingBonuses(turnId, clan);

            // tavern offers
            tavernService.prepareTavernOffers(clan, getCharacterFilter(clan));

            // food consumption
            performFoodConsumption(turnId, clan);

            // perform character healing
            performCharacterHealing(turnId, clan);

            // perform character leveling
            performCharacterLeveling(turnId, clan);

            // mark characters as ready
            markCharactersReady(clan);

            // reset arena
            clan.setArena(false);
        }
    }

    @Transactional
    @Override
    public void changeDefaultAction(DefaultActionRequest request, int clanId) {
        Clan clan = clanRepository.getOne(clanId);
        clan.setDefaultAction(request.getDefaultAction());
    }

    @Override
    public List<TavernOffer> loadTavernOffers(int clanId) {
        Clan clan = clanRepository.getOne(clanId);
        return tavernService.loadTavernOffers(TavernOffer.State.AVAILABLE, clan);
    }

    private void deleteDeadCharacters(Clan clan) {
        Iterator<Character> iterator = clan.getCharacters().iterator();
        while (iterator.hasNext()) {
            Character character = iterator.next();
            if (character.getHitpoints() < 1) {
                log.debug("Deleting character {}.", character.getName());
                iterator.remove();
                character.getClan().getCharacters().remove(character);

                characterService.delete(character);
            }
        }
    }

    private void performFoodConsumption(int turnId, Clan clan) {
        log.debug("Food consumption for clan {}.", clan.getId());

        if (clan.getCharacters().isEmpty()) {
            return;
        }

        ClanNotification notification = new ClanNotification(turnId, clan.getId());
        notification.setHeader(clan.getName());
        notification.setFoodIncome(0);
        notification.setInjury(0);
        notificationService.addLocalizedTexts(notification.getText(), "clan.foodConsumption", new String[] {});

        Iterator<Character> iterator = clan.getCharacters().iterator();
        while (iterator.hasNext()) {
            Character character = iterator.next();

            // ascetic
            if (character.getTraits().containsKey(TraitDetails.TraitName.ASCETIC)) {
                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.trait.ascetic", new String[] {character.getName()});
                notification.getDetails().add(detail);

                continue; // skip food consumption
            }

            if (clan.getFood() >= FOOD_CONSUMPTION) {
                clan.setFood(clan.getFood() - FOOD_CONSUMPTION);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.foodConsumption",
                        new String[] {character.getName()});
                notification.getDetails().add(detail);
                notification.setFoodIncome(notification.getFoodIncome() - FOOD_CONSUMPTION);
            } else {
                log.debug("Character {} is starving,", character.getName());

                character.setHitpoints(character.getHitpoints() - 1);
                character.setState(CharacterState.STARVING);

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.starving", new String[] {character.getName()});
                notification.getDetails().add(detail);
                notification.setInjury(notification.getInjury() + 1);

                if (character.getHitpoints() < 1) {
                    log.debug("Character {} died of starvation.", character.getName());

                    NotificationDetail detailDeath = new NotificationDetail();
                    notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.starvationdeath",
                            new String[] {character.getName()});
                    notification.getDetails().add(detailDeath);

                    // delete and remove from clan
                    characterService.delete(character);
                    iterator.remove();
                }
            }
        }

        notificationService.save(notification);
    }

    private void performCharacterHealing(int turnId, Clan clan) {
        for (Character character : clan.getCharacters()) {
            // heal only resting characters
            if (!(character.getState().equals(CharacterState.READY) || character.getState().equals(CharacterState.RESTING))) {
                continue;
            }

            // skip npc characters
            if (character.getClan() == null) {
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
            int points = HEALING;
            if (character.getClan().getBuildings().containsKey(BuildingDetails.BuildingName.SICK_BAY)) {
                Building building = character.getClan().getBuildings().get(BuildingDetails.BuildingName.SICK_BAY);
                int bonus = building.getLevel() * HEALING;
                points += bonus;

                if (building.getLevel() > 0) {
                    NotificationDetail healingBuildingDetail = new NotificationDetail();
                    notificationService.addLocalizedTexts(healingBuildingDetail.getText(), "detail.healing.building",
                            new String[]{String.valueOf(bonus)});
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

    private void performCharacterLeveling(int turnId, Clan clan) {
        for (Character character : clan.getCharacters()) {
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
                notification.setImageUrl(character.getImageUrl());
                notificationService.addLocalizedTexts(notification.getText(), "character.levelup", new String[]{character.getName()});

                // add random trait to character
                Trait trait = traitService.getRandomTrait(character);
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

    private void markCharactersReady(Clan clan) {
        for (Character character : clan.getCharacters()) {
            character.setState(CharacterState.READY);
        }
    }

    private CharacterFilter getCharacterFilter(Clan clan) {
        CharacterFilter filter = new CharacterFilter();

        for (Character character : clan.getCharacters()) {
            filter.getNames().add(character.getName());
            filter.getAvatars().add(character.getImageUrl());
        }

        return filter;
    }

}
