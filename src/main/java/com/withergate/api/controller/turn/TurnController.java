package com.withergate.api.controller.turn;

import com.withergate.api.model.turn.Turn;
import com.withergate.api.repository.TurnRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Turn controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class TurnController {

    private final TurnRepository turnRepository;

    public TurnController(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    /**
     * Retrieves the current turn's data.
     *
     * @return the current turn
     */
    @GetMapping("/turn")
    public ResponseEntity<Turn> getTurn() {
        return new ResponseEntity<>(turnRepository.findFirstByOrderByTurnIdDesc(), HttpStatus.OK);
    }
}
