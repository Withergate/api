package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.quest.QuestService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Clan service. Handles all basic operations over the clan entity.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class ClanServiceImpl implements ClanService {

    public static final int INFORMATION_QUOTIENT = 10;
    public static final int BASIC_POPULATION_LIMIT = 6;

    private final ClanRepository clanRepository;
    private final CharacterService characterService;
    private final GameProperties gameProperties;
    private final NotificationService notificationService;
    private final QuestService questService;

    public ClanServiceImpl(ClanRepository clanRepository, CharacterService characterService,
                           GameProperties gameProperties,
                           NotificationService notificationService,
                           QuestService questService) {
        this.clanRepository = clanRepository;
        this.characterService = characterService;
        this.gameProperties = gameProperties;
        this.notificationService = notificationService;
        this.questService = questService;
    }

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
    public Clan createClan(int clanId, ClanRequest clanRequest) throws EntityConflictException {
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
        clan.setFame(0);
        clan.setCaps(50);
        clan.setJunk(20);
        clan.setFood(20);
        clan.setInformation(0);
        clan.setInformationLevel(0);
        clan.setCharacters(new HashSet<>());

        // assign random initial characters to clan.
        for (int i = 0; i < gameProperties.getInitialClanSize(); i++) {
            Character character = characterService.generateRandomCharacter();
            character.setClan(clan);
            clan.getCharacters().add(character);
        }

        clan.setBuildings(new HashMap<>());

        return clanRepository.save(clan);
    }

    @Override
    public Character hireCharacter(Clan clan) {
        log.debug("Hiring new character for clan {}", clan.getId());

        // create a random character
        Character character = characterService.generateRandomCharacter();
        character.setClan(clan);

        // deduct price and add the character to the clan
        clan.getCharacters().add(character);

        log.debug("Hired new character: {}", character.getName());
        return character;
    }

    @Override
    public void clearArenaCharacters() {
        log.debug("Clearing arena characters...");
        List<Clan> clans = clanRepository.findAll();

        for (Clan clan : clans) {
            clan.setArena(false);
        }
    }

    @Override
    public void increaseInformationLevel(Clan clan, ClanNotification notification, int informationLevel) {
        log.debug("Increasing clan's information level for clan: {}", clan.getName());

        // handle level up
        clan.setInformationLevel(informationLevel);

        NotificationDetail detail = new NotificationDetail();
        notificationService.addLocalizedTexts(detail.getText(), "detail.information.levelup", new String[]{});
        notification.getDetails().add(detail);

        // assign quests
        questService.assignQuests(clan, notification, informationLevel);
    }
}
