package com.withergate.api.service.action;

/**
 * Actionable interface.
 *
 * @author Martin Myslik
 */
public interface Actionable {

    void runActions(int turn);

    int getOrder();

}
