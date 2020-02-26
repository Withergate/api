package com.withergate.api.controller.clan;

import java.security.Principal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.dto.ClanIntelDTO;
import com.withergate.api.game.model.request.ActionCancelRequest;
import com.withergate.api.game.model.request.ClanRequest;
import com.withergate.api.game.model.request.DefaultActionRequest;
import com.withergate.api.game.model.request.RenameRequest;
import com.withergate.api.game.model.statistics.ClanTurnStatistics;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.game.model.view.Views;
import com.withergate.api.game.repository.TurnRepository;
import com.withergate.api.game.repository.statistics.ClanTurnStatisticsRepository;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.exception.ValidationException;
import com.withergate.api.service.location.TavernService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clan controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class ClanController {

    private final ClanService clanService;
    private final TavernService tavernService;
    private final TurnRepository turnRepository;
    private final ClanTurnStatisticsRepository statisticsRepository;
    private final CharacterService characterService;

    /**
     * Retrieves the clan for the authenticated player.
     *
     * @param principal the principal
     * @return the clan matching the id of the authenticated user
     */
    @GetMapping("/clan")
    public ResponseEntity<Clan> getClan(Principal principal) {
        Clan clan = clanService.getClan(Integer.parseInt(principal.getName()));

        if (clan == null) {
            log.warn("Clan with this ID does not exist yet. It should be created first!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(clan, HttpStatus.OK);
    }

    /**
     * Retrieves intel about the specified clan.
     *
     * @param principal the principal
     * @param targetId target clan's ID
     * @return the clan matching the id of the authenticated user
     */
    @GetMapping("/clan/{id}")
    public ResponseEntity<ClanIntelDTO> getClanIntel(Principal principal, @PathVariable("id") int targetId) throws InvalidActionException {
        return new ResponseEntity<>(clanService.getClanIntel(targetId, Integer.parseInt(principal.getName())), HttpStatus.OK);
    }

    /**
     * Creates a new clan for the authenticated player. If this player already has a clan, returns error status.
     *
     * @param principal   the principal
     * @param clanRequest the clan request containing necessary clan details
     * @return the created clan
     * @throws EntityConflictException entity conflict
     */
    @PostMapping("/clan")
    public ResponseEntity<Clan> createClan(Principal principal, @RequestBody ClanRequest clanRequest)
            throws EntityConflictException, ValidationException {
        log.debug("Creating a new clan for player {}", principal.getName());

        Turn turn = turnRepository.findFirstByOrderByTurnIdDesc();
        Clan clan = clanService.createClan(Integer.parseInt(principal.getName()), clanRequest, turn.getTurnId());
        return new ResponseEntity<>(clan, HttpStatus.CREATED);
    }

    /**
     * Retrieves the list of all clans. Supports paging and sorting. This view contains only a limited subset of
     * information about each clan.
     *
     * @param pageable pagination and sorting
     * @return the page containing clans in the specified order
     */
    @JsonView(Views.Public.class)
    @GetMapping("/clans")
    public ResponseEntity<Page<Clan>> getClans(Pageable pageable) {
        return new ResponseEntity<>(clanService.getClans(pageable), HttpStatus.OK);
    }

    /**
     * Retrieves the list of available tavern offers.
     *
     * @return the list of offers
     */
    @JsonView(Views.Public.class)
    @GetMapping("/clan/tavernOffers")
    public ResponseEntity<List<TavernOffer>> getClanTavernOffers(Principal principal) {
        return new ResponseEntity<>(clanService.loadTavernOffers(Integer.parseInt(principal.getName())), HttpStatus.OK);
    }

    /**
     * Refreshes tavern offers for clan.
     */
    @PostMapping("/clan/tavernOffers/refresh")
    public ResponseEntity<Void> refreshTavernOffers(Principal principal) throws InvalidActionException {
        tavernService.refreshTavernOffers(Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Changes clan's default action.
     *
     * @param principal   the principal
     * @param request the clan request containing necessary clan details
     */
    @PutMapping("/clan/defaultAction")
    public ResponseEntity<Void> changeDefaultAction(Principal principal, @RequestBody DefaultActionRequest request) {
        clanService.changeDefaultAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves clan statistics..
     *
     * @return the list of clan turn statistics
     */
    @JsonView(Views.Public.class)
    @GetMapping("/clan/statistics")
    public ResponseEntity<List<ClanTurnStatistics>> getClanStatistics(Principal principal) {
        return new ResponseEntity<>(statisticsRepository.findAllByClanIdOrderByTurnIdAsc(Integer.parseInt(principal.getName())),
                HttpStatus.OK);
    }

    /**
     * Cancels given action if supported.
     */
    @PostMapping("/character/action/cancel")
    public ResponseEntity<Void> cancelAction(Principal principal, @RequestBody ActionCancelRequest request) throws InvalidActionException {
        characterService.cancelAction(request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Renames clan's defender. Premium feature.
     *
     * @param principal   the principal
     * @param request the request with new name
     */
    @PutMapping("/clan/defender")
    public ResponseEntity<Void> renameDefender(Principal principal, @RequestBody RenameRequest request) throws InvalidActionException {
        log.debug("Renaming defender for clan {}", principal.getName());

        clanService.renameDefender(Integer.parseInt(principal.getName()), request.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
