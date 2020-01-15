package com.withergate.api.service.turn;

import com.withergate.api.model.turn.Turn;

/**
 * Turn service.
 *
 * @author Martin Myslik
 */
public interface TurnService {

    /**
     * Gets the current turn.
     *
     * @return current turn
     */
    Turn getCurrentTurn();

    /**
     * Saves the provided turn.
     *
     * @param turn turn
     */
    void saveTurn(Turn turn);

}
