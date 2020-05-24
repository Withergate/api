package com.withergate.api.scheduling;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.withergate.api.service.action.Actionable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Action registrar. Responsible for the evaluation of all game actions in the correct order.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ActionRegistrar {

    private final List<Actionable> actionables;

    /**
     * Runs all game actions.
     *
     * @param turn turn
     */
    public void runActions(int turn) {
        log.debug("-> Running all actions");

        Collections.sort(actionables, Comparator.comparing(Actionable::getOrder));
        for (Actionable actionable : actionables) {
            log.debug("* Running action for {} with order {}.", actionable.getClass().getSimpleName(), actionable.getOrder());
            actionable.runActions(turn);
        }
    }

}
