package com.withergate.api.controller.clan;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.model.turn.Turn;
import com.withergate.api.model.view.Views;
import com.withergate.api.repository.TurnRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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

}
