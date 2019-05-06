package com.withergate.api.service.location;

import com.withergate.api.model.action.TavernAction;

/**
 * Tavern service interface.
 *
 * @author Martin Myslik
 */
public interface TavernService {

    /**
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveTavernAction(TavernAction action);

    /**
     * Handles all pending tavern actions.
     *
     * @param turnId turn ID
     */
    void processTavernActions(int turnId);

}
