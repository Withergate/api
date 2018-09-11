package com.withergate.api.model.view;

/**
 * Class used for the specification of class views during JSON serialization/deserialization.
 *
 * @author Martin Myslik
 */
public class Views {

    private Views() {
        // disable constructor
    }

    public static class Public {
        // public view
    }

    public static class Internal extends Public {
        // internal view
    }
}