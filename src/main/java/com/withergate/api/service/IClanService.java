package com.withergate.api.service;

import com.withergate.api.model.Clan;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.service.exception.EntityConflictException;

/**
 * ClanService interface.
 *
 * @author Martin Myslik
 */
public interface IClanService {

    /**
     * Retrieves a clan by its ID.
     *
     * @param clanId the clan ID
     * @return the clan or null if not found
     */
    Clan getClan(int clanId);

    /**
     * Creates a new clan for the provided ID and name.
     *
     * @param clanId      the clan ID
     * @param clanRequest the clan request date
     * @return the created clan
     * @throws EntityConflictException
     */
    Clan createClan(int clanId, ClanRequest clanRequest) throws EntityConflictException;
}
