package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.Clan.DefaultAction;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;
import com.withergate.api.service.research.ResearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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

    private static final int INITIAL_FOOD = 20;
    private static final int INITIAL_JUNK = 20;
    private static final int INITIAL_CAPS = 50;
    private static final int TURN_INCREMENT = 3;

    private static final int CLAN_NAME_MIN_LENGTH = 6;
    private static final int CLAN_NAME_MAX_LENGTH = 30;

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final NotificationService notificationService;
    private final QuestService questService;
    private final BuildingService buildingService;
    private final ResearchService researchService;
    private final TavernService tavernService;
    private final RandomService randomService;
    private final TraitService traitService;
    private final GameProperties gameProperties;
    private final TurnRepository turnRepository;

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
        if (clanRequest.getName().length() < CLAN_NAME_MIN_LENGTH || clanRequest.getName().length() > CLAN_NAME_MAX_LENGTH) {
            throw new ValidationException("Clan name must be between 6 and 30 characters long.");
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

        // get current turn
        Turn turn = turnRepository.findFirstByOrderByTurnIdDesc();

        // Create clan with initial resources.
        Clan clan = new Clan();
        clan.setId(clanId);
        clan.setName(clanRequest.getName());
        clan.setLastActivity(LocalDateTime.now());
        clan.setFame(0);
        clan.setCaps(INITIAL_CAPS + getStartingResourceBonus(turn.getTurnId()));
        clan.setJunk(INITIAL_JUNK + getStartingResourceBonus(turn.getTurnId()));
        clan.setFood(INITIAL_FOOD + getStartingResourceBonus(turn.getTurnId()));
        clan.setInformation(0);
        clan.setInformationLevel(0);
        clan.setCharacters(new HashSet<>());
        clan.setDefaultAction(DefaultAction.EXPLORE_NEIGHBORHOOD);

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

        // assign research
        clan.setResearch(new HashMap<>());
        researchService.assignResearch(clan);

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

            // check information level
            checkInformationLevel(turnId, clan);

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

            if (clan.getFood() >= gameProperties.getFoodConsumption()) {
                clan.setFood(clan.getFood() - gameProperties.getFoodConsumption());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.foodConsumption",
                        new String[] {character.getName()});
                notification.getDetails().add(detail);
                notification.changeFood(- gameProperties.getFoodConsumption());
            } else {
                log.debug("Character {} is starving,", character.getName());

                character.changeHitpoints(- gameProperties.getStarvationInjury());
                character.setState(CharacterState.STARVING);

                // lose fame
                clan.changeFame(- gameProperties.getStarvationFame());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.starving", new String[] {character.getName()});
                notification.getDetails().add(detail);
                notification.changeFame(- gameProperties.getStarvationFame());
                notification.changeInjury(gameProperties.getStarvationInjury());

                if (character.getHitpoints() < 1) {
                    log.debug("Character {} died of starvation.", character.getName());

                    NotificationDetail detailDeath = new NotificationDetail();
                    notificationService.addLocalizedTexts(detailDeath.getText(), "detail.character.starvationdeath",
                            new String[] {character.getName()});
                    notification.getDetails().add(detailDeath);
                    notification.setDeath(true);

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
            int points = gameProperties.getHealing();
            if (character.getClan().getBuildings().containsKey(BuildingDetails.BuildingName.SICK_BAY)) {
                Building building = character.getClan().getBuildings().get(BuildingDetails.BuildingName.SICK_BAY);
                int bonus = building.getLevel() * gameProperties.getHealing();
                points += bonus;

                if (building.getLevel() > 0) {
                    NotificationDetail healingBuildingDetail = new NotificationDetail();
                    notificationService.addLocalizedTexts(healingBuildingDetail.getText(), "detail.healing.building",
                            new String[] {String.valueOf(bonus)});
                    notification.getDetails().add(healingBuildingDetail);
                }
            }

            // lizard trait
            if (character.getTraits().containsKey(TraitName.LIZARD)) {
                points += character.getTraits().get(TraitName.LIZARD).getDetails().getBonus();
                NotificationDetail lizardDetail = new NotificationDetail();
                notificationService.addLocalizedTexts(lizardDetail.getText(), "detail.trait.lizard",
                        new String[]{character.getName()}, character.getTraits().get(TraitName.LIZARD).getDetails().getName());
                notification.getDetails().add(lizardDetail);
            }

            int healing = Math.min(points, hitpointsMissing);
            character.changeHitpoints(healing);

            notificationService.addLocalizedTexts(notification.getText(), "character.healing", new String[] {});
            notification.changeHealing(healing);

            notificationService.save(notification);
        }
    }

    private void performCharacterLeveling(int turnId, Clan clan) {
        for (Character character : clan.getCharacters()) {
            if (character.getExperience() >= character.getNextLevelExperience()) {
                // level up
                log.debug("Character {} leveled up.", character.getName());

                character.changeExperience(- character.getNextLevelExperience());
                int hpIncrease = randomService.getRandomInt(1, RandomServiceImpl.K6);
                character.setMaxHitpoints(character.getMaxHitpoints() + hpIncrease);
                character.changeHitpoints(hpIncrease);
                character.setLevel(character.getLevel() + 1);

                // notification
                ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
                notification.setHeader(character.getName());
                notification.setImageUrl(character.getImageUrl());
                notificationService.addLocalizedTexts(notification.getText(), "character.levelup", new String[] {character.getName()});

                // add random trait to character
                Trait trait = traitService.getRandomTrait(character);
                character.getTraits().put(trait.getDetails().getIdentifier(), trait);
                log.debug("New trait assigned to {}: {}", character.getName(), trait.getDetails().getIdentifier());

                NotificationDetail detail = new NotificationDetail();
                notificationService.addLocalizedTexts(detail.getText(), "detail.character.levelup.trait",
                        new String[] {character.getName()});
                notification.getDetails().add(detail);

                // save
                notificationService.save(notification);
            }
        }
    }

    private void checkInformationLevel(int turnId, Clan clan) {
        log.debug("Increasing clan's information level for clan: {}", clan.getName());

        // handle next level
        if (clan.getInformation() >= clan.getNextLevelInformation()) {
            clan.changeInformation(- clan.getNextLevelInformation());
            clan.setInformationLevel(clan.getInformationLevel() + 1);

            ClanNotification notification = new ClanNotification(turnId, clan.getId());
            notification.setHeader(clan.getName());
            notificationService.addLocalizedTexts(notification.getText(), "clan.information.levelup", new String[] {});

            // assign quests
            questService.assignQuests(clan, notification);

            // save notification
            notificationService.save(notification);
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

    /*
     * When starting game later, players should receive bonus resources to mitigate the balance issues.
     */
    private int getStartingResourceBonus(int turn) {
        return (turn - 1) * TURN_INCREMENT;
    }

}
