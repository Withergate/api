package com.withergate.api.service.clan;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.Clan.DefaultAction;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.research.ResearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
    public static final int MAX_CHARACTER_STRENGTH = 17;

    private static final int INITIAL_FOOD = 20;
    private static final int INITIAL_JUNK = 20;
    private static final int INITIAL_CAPS = 50;
    private static final int TURN_INCREMENT = 3;

    private static final int CLAN_NAME_MIN_LENGTH = 6;
    private static final int CLAN_NAME_MAX_LENGTH = 30;

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final BuildingService buildingService;
    private final ResearchService researchService;
    private final TavernService tavernService;
    private final ClanTurnService clanTurnService;
    private final RandomService randomService;

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
    public Clan createClan(int clanId, ClanRequest clanRequest, int turn) throws EntityConflictException, ValidationException {
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

        // Create clan with initial resources.
        Clan clan = new Clan();
        clan.setId(clanId);
        clan.setName(clanRequest.getName());
        clan.setLastActivity(LocalDateTime.now());
        clan.setFame(0);
        clan.setCaps(INITIAL_CAPS + getStartingResourceBonus(turn));
        clan.setJunk(INITIAL_JUNK + getStartingResourceBonus(turn));
        clan.setFood(INITIAL_FOOD + getStartingResourceBonus(turn));
        clan.setInformation(0);
        clan.setInformationLevel(0);
        clan.setCharacters(new HashSet<>());
        clan.setDefaultAction(DefaultAction.EXPLORE_NEIGHBORHOOD);
        clan.setPreferDisaster(true);

        // assign random initial characters to clan.
        CharacterFilter filter = new CharacterFilter();
        for (int i = 0; i < INITIAL_CLAN_SIZE; i++) {
            Character character = characterService.generateRandomCharacter(filter,
                    randomService.getRandomAttributeCombination(MAX_CHARACTER_STRENGTH - i * 2));
            character.setClan(clan);
            clan.getCharacters().add(character);

            // filter out used avatars
            filter.getAvatars().add(character.getImageUrl());
            filter.getNames().add(character.getName());
        }

        // set buildings
        for (BuildingDetails details : buildingService.getAllBuildingDetails()) {
            Building building = new Building();
            building.setProgress(0);
            building.setLevel(0);
            building.setDetails(details);
            building.setClan(clan);
            clan.getBuildings().add(building);
        }

        // assign research
        researchService.assignResearch(clan);

        clan = clanRepository.save(clan);

        // prepare tavern offers
        tavernService.prepareTavernOffers(clan);

        return clan;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void performClanTurnUpdates(int turnId) {
        for (Clan clan : getAllClans()) {
            clanTurnService.performClanTurnUpdates(clan, turnId);
        }
    }

    @Transactional
    @Override
    public void changeDefaultAction(DefaultActionRequest request, int clanId) {
        Clan clan = clanRepository.getOne(clanId);
        clan.setDefaultAction(request.getDefaultAction());
        clan.setPreferDisaster(request.isPreferDisaster());
    }

    @Override
    public List<TavernOffer> loadTavernOffers(int clanId) {
        Clan clan = clanRepository.getOne(clanId);
        return tavernService.loadTavernOffers(TavernOffer.State.AVAILABLE, clan);
    }

    /*
     * When starting game later, players should receive bonus resources to mitigate the balance issues.
     */
    private int getStartingResourceBonus(int turn) {
        return (turn - 1) * TURN_INCREMENT;
    }

}
