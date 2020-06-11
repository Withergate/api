package com.withergate.api.service.clan;

import java.util.HashSet;
import java.util.List;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterFilter;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.dto.ClanIntelDTO;
import com.withergate.api.game.model.request.ClanRequest;
import com.withergate.api.game.model.statistics.ClanTurnStatistics;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.game.repository.statistics.ClanTurnStatisticsRepository;
import com.withergate.api.profile.model.PremiumType;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.building.BuildingService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
import com.withergate.api.service.premium.Premium;
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

    public static final int INFORMATION_QUOTIENT = 12;
    public static final int BASIC_POPULATION_LIMIT = 6;
    public static final int INITIAL_CLAN_SIZE = 5;
    public static final int MAX_CHARACTER_STRENGTH = 17;

    private static final int INITIAL_FOOD = 20;
    private static final int INITIAL_JUNK = 20;
    private static final int INITIAL_CAPS = 50;
    private static final int TURN_INCREMENT = 3;

    private static final int CLAN_NAME_MIN_LENGTH = 6;
    private static final int CLAN_NAME_MAX_LENGTH = 30;
    private static final int DEFENDER_MIN_LENGTH = 5;
    private static final int DEFENDER_MAX_LENGTH = 16;
    private static final String DEFAULT_DEFENDER_NAME = "Doggo";

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final BuildingService buildingService;
    private final ResearchService researchService;
    private final TavernService tavernService;
    private final ClanTurnService clanTurnService;
    private final RandomService randomService;
    private final ClanTurnStatisticsRepository statisticsRepository;
    private final GameProperties properties;

    @Override
    public Clan getClan(int clanId) {
        return clanRepository.findById(clanId).orElse(null);
    }

    @Override
    public List<Clan> getAllClans() {
        return clanRepository.findAll();
    }

    @Override
    public List<Clan> getAllClansByFame() {
        return clanRepository.findAllByOrderByFameDescIdAsc();
    }

    @Override
    public Page<Clan> getClans(Pageable pageable) {
        return clanRepository.findAllByOrderByFameDescIdAsc(pageable);
    }

    @Override
    public Clan saveClan(Clan clan) {
        return clanRepository.save(clan);
    }

    @Transactional
    @Override
    public Clan createClan(int clanId, ClanRequest clanRequest, int turn) throws EntityConflictException, ValidationException {
        // sanitize
        String name = clanRequest.getName().replaceAll("\\s+", " ").trim();

        // validate clan name
        if (name.length() < CLAN_NAME_MIN_LENGTH || name.length() > CLAN_NAME_MAX_LENGTH) {
            throw new ValidationException("Clan name must be between 6 and 30 characters long.");
        }

        // check if clan already exists.
        if (getClan(clanId) != null) {
            log.warn("Cannot create a clan with ID {}", clanId);
            throw new EntityConflictException("Clan with the provided ID already exists.");
        }

        // check for name collisions. Clan name must be unique among all players.
        if (clanRepository.findOneByName(name) != null) {
            log.warn("Cannot create a clan with name {}", clanRequest.getName());
            throw new EntityConflictException("Clan with the provided name already exists.");
        }

        // Create clan with initial resources.
        Clan clan = new Clan();
        clan.setId(clanId);
        clan.setName(name);
        clan.setFame(0);
        clan.setCaps(INITIAL_CAPS + getStartingResourceBonus(turn));
        clan.setJunk(INITIAL_JUNK + getStartingResourceBonus(turn));
        clan.setFood(INITIAL_FOOD + getStartingResourceBonus(turn));
        clan.setDisasterProgress(turn - 1);
        clan.setInformation(0);
        clan.setInformationLevel(0);
        clan.setCharacters(new HashSet<>());
        clan.setDefenderName(DEFAULT_DEFENDER_NAME);

        // assign random initial characters to clan.
        CharacterFilter filter = new CharacterFilter();
        for (int i = 0; i < INITIAL_CLAN_SIZE; i++) {
            int sum = MAX_CHARACTER_STRENGTH - i * 2;
            Character character = characterService.generateRandomCharacter(filter,
                    randomService.getRandomAttributeCombination(sum, clanRequest.getType()));
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

    @Override
    public List<TavernOffer> loadTavernOffers(int clanId) {
        Clan clan = clanRepository.getOne(clanId);
        return tavernService.loadTavernOffers(TavernOffer.State.AVAILABLE, clan);
    }

    @Retryable
    @Override
    public void prepareStatistics(int turnId) {
        log.debug("--> Preparing turn statistics");
        for (Clan clan : getAllClans()) {
            ClanTurnStatistics statistics = new ClanTurnStatistics(clan, turnId);
            statisticsRepository.save(statistics);
        }
    }

    @Override
    public ClanIntelDTO getClanIntel(int targetId, int spyId) throws InvalidActionException {
        Clan target = getClan(targetId);
        Clan spy = getClan(spyId);
        if (target == null || spy == null) {
            throw new InvalidActionException("One or both of provided clans do not exist!");
        }

        return new ClanIntelDTO(target, spy);
    }

    @Transactional
    @Premium(type = PremiumType.SILVER)
    @Override
    public void renameDefender(int clanId, String name) throws InvalidActionException {
        // get clan
        Clan clan = getClan(clanId);

        // sanitize
        name = name.replaceAll("\\s+", " ").trim();

        // check name length
        if (name.length() < DEFENDER_MIN_LENGTH || name.length() > DEFENDER_MAX_LENGTH) {
            throw new InvalidActionException("Name must be between " + DEFENDER_MIN_LENGTH + " and " + DEFENDER_MAX_LENGTH
                    + " characters long");
        }

        clan.setDefenderName(name);
    }

    @Transactional
    @Override
    public void processLoan(int clanId) throws InvalidActionException {
        Clan clan = getClan(clanId);

        if (clan.isActiveLoan()) { // payback
            // check price
            if (clan.getCaps() < properties.getLoanPayback()) {
                throw new InvalidActionException("Not enough caps to perform this action.");
            }

            // pay price
            clan.changeCaps(- properties.getLoanCaps());
            clan.changeFame(properties.getLoanFame());
            clan.setActiveLoan(false);
        } else { // get loan
            clan.changeCaps(properties.getLoanCaps());
            clan.changeFame(- properties.getLoanFame());
            clan.setActiveLoan(true);
        }
    }

    /*
     * When starting game later, players should receive bonus resources to mitigate the balance issues.
     */
    private int getStartingResourceBonus(int turn) {
        return (turn - 1) * TURN_INCREMENT;
    }

}
