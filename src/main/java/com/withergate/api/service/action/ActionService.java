package com.withergate.api.service.action;

import java.util.List;

/**
 * ActionService interface.
 *
 * @author Martin Myslik
 */
public interface ActionService {

    /**
     * Performs all actions reserved for the end of the game.
     *
     * @param turnId turn ID
     */
    void performFactionFameDistribution(int turnId, List<Integer> turns);

    /**
     * Assign default actions to all characters.
     */
    void assignDefaultActions();

}
