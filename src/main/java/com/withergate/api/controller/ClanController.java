package com.withergate.api.controller;

import com.withergate.api.model.Clan;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.service.clan.IClanService;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Clan controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class ClanController {

    private final IClanService clanService;

    /**
     * Constructor.
     *
     * @param clanService clan service
     */
    public ClanController(IClanService clanService) {
        this.clanService = clanService;
    }

    /**
     * Retrieves the clan for the authenticated player.
     *
     * @param principal the principal
     * @return the clan matching the id of the authenticated user
     */
    @RequestMapping("/clan")
    public ResponseEntity<Clan> getClan(Principal principal) {
        log.debug("Requesting clan for player {}", principal.getName());

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
     */
    @RequestMapping(value = "/clan", method = RequestMethod.POST)
    public ResponseEntity<Clan> createClan(Principal principal, @RequestBody ClanRequest clanRequest) {
        log.debug("Creating a new clan for player {}", principal.getName());

        try {
            Clan clan = clanService.createClan(Integer.parseInt(principal.getName()), clanRequest);
            return new ResponseEntity<>(clan, HttpStatus.CREATED);
        } catch (EntityConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
