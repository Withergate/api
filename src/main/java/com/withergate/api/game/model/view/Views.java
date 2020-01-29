package com.withergate.api.game.model.view;

/**
 * Class used for the specification of class views during JSON serialization/deserialization.
 *
 * @author Martin Myslik
 */
public class Views {

    private Views() {
        // disable constructor
    }

    /**
     * Public view.
     */
    public static class Public {
        // public view
    }


    /**
     * Internal view.
     */
    public static class Internal extends Public {
        // internal view
    }
}
