package com.withergate.api.service.turn;

import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.game.repository.TurnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Turn service implementation.
 *
 * @author Martin Myslik
 */
@RequiredArgsConstructor
@Service
public class TurnServiceImpl implements TurnService {

    private final TurnRepository turnRepository;

    @Override
    public Turn getCurrentTurn() {
        return turnRepository.findFirstByOrderByTurnIdDesc();
    }

    @Override
    public void saveTurn(Turn turn) {
        turnRepository.save(turn);
    }

}
