package com.withergate.api.service.exception;

/**
 * EntityConflict exception. Used when there is a conflict when saving or updating an entity.
 *
 * @author Martin Myslik
 */
public class EntityConflictException extends Exception {

    public EntityConflictException(String message) {
        super(message);
    }
}
