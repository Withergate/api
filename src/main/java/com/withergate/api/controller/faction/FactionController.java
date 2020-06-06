package com.withergate.api.controller.faction;

import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.model.faction.FactionsOverview;
import com.withergate.api.game.model.request.ClanCombatRequest;
import com.withergate.api.game.model.request.FactionRequest;
import com.withergate.api.service.combat.ClanCombatService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.faction.FactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Faction controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class FactionController {

    private final FactionService factionService;
    private final ClanCombatService clanCombatService;

    /**
     * Retrieves all factions.
     *
     * @return the clan matching the id of the authenticated user
     */
    @GetMapping("/factions")
    public ResponseEntity<List<Faction>> getFactions() {
        return new ResponseEntity<>(factionService.getFactions(), HttpStatus.OK);
    }

    /**
     * Submits a new faction action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the faction action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/factions/action")
    public ResponseEntity<Void> submitFactionAction(Principal principal, @RequestBody FactionRequest request)
            throws InvalidActionException {

        factionService.saveAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves overview of factions.
     *
     * @return factions overview
     */
    @GetMapping("/factions/overview")
    public ResponseEntity<FactionsOverview> getFactionsOverview(Principal principal) {
        return new ResponseEntity<>(factionService.getOverview(Integer.parseInt(principal.getName())), HttpStatus.OK);
    }

    /**
     * Submits a new combat action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the clan combat action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/factions/action/combat")
    public ResponseEntity<Void> submitClanCombatAction(Principal principal, @RequestBody ClanCombatRequest request)
            throws InvalidActionException {

        clanCombatService.saveAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
