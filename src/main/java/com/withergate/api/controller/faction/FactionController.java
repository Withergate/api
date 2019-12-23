package com.withergate.api.controller.faction;

import com.withergate.api.model.faction.Faction;
import com.withergate.api.service.faction.FactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Retrieves all factions.
     *
     * @return the clan matching the id of the authenticated user
     */
    @GetMapping("/factions")
    public ResponseEntity<List<Faction>> getFactions() {
        return new ResponseEntity<>(factionService.getFactions(), HttpStatus.OK);
    }

}
