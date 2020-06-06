package com.withergate.api.controller.arena;

import java.security.Principal;
import java.util.List;

import com.withergate.api.game.model.arena.ArenaStats;
import com.withergate.api.game.model.request.ArenaRequest;
import com.withergate.api.game.repository.arena.ArenaStatsRepository;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.location.ArenaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Arena controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class ArenaController {

    private final ArenaService arenaService;
    private final ArenaStatsRepository arenaStatsRepository;

    /**
     * Submits a new arena action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the arena action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/arena/action")
    public ResponseEntity<Void> visitArena(Principal principal, @RequestBody ArenaRequest request) throws InvalidActionException {
        log.debug("Executing arena action for player {}", principal.getName());

        arenaService.saveAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieves the list of top 10 arena results.
     *
     * @return the page containing arena stats
     */
    @GetMapping("/arena/stats")
    public ResponseEntity<List<ArenaStats>> getStats() {
        return new ResponseEntity<>(arenaStatsRepository.findTop10ByOrderByStatsDesc(), HttpStatus.OK);
    }

}
