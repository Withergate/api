package com.withergate.api.service.clan;

import com.withergate.api.Constants;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.exception.EntityConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Clan service. Handles all basic operations over the clan entity.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class ClanService implements IClanService {

    private final ClanRepository clanRepository;
    private final CharacterService characterService;

    /**
     * Constructor.
     *
     * @param clanRepository   clan repository
     * @param characterService character service
     */
    public ClanService(ClanRepository clanRepository, CharacterService characterService) {
        this.clanRepository = clanRepository;
        this.characterService = characterService;
    }

    @Override
    public Clan getClan(int clanId) {
        return clanRepository.findById(clanId).orElse(null);
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
        clan.setJunk(100);
        clan.setCharacters(new ArrayList<>());

        // assign random initial characters to clan.
        for (int i = 0; i < Constants.INITIAL_CLAN_SIZE; i++) {
            Character character = characterService.generateRandomCharacter();
            character.setClan(clan);
            clan.getCharacters().add(character);
        }

        return clanRepository.save(clan);
    }

    @Transactional
    @Override
    public Character hireCharacter(Clan clan) {
        log.debug("Hiring new character for clan {}", clan.getId());

        // create a random character
        Character character = characterService.generateRandomCharacter();
        character.setClan(clan);

        // deduct price and add the character to the clan
        clan.getCharacters().add(character);
        clanRepository.save(clan);

        log.debug("Hired new character: {}", character.getName());
        return character;
    }
}
