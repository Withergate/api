package com.withergate.api.controller.turn;

import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.service.turn.TurnService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RestController
public class TurnController {

    private final TurnService turnService;

    /**
     * Retrieves the current turn's data.
     *
     * @return the current turn
     */
    @GetMapping("/turn")
    public ResponseEntity<Turn> getTurn() {
        return new ResponseEntity<>(turnService.getCurrentTurn(), HttpStatus.OK);
    }
}
