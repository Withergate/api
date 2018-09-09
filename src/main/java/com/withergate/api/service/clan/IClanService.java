package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
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
     * Saves the provided clan.
     *
     * @param clan the clan to be saved or updated
     * @return the saved clan
     */
    Clan saveClan(Clan clan);

    /**
     * Creates a new clan for the provided ID and name.
     *
     * @param clanId      the clan ID
     * @param clanRequest the clan request date
     * @return the created clan
     * @throws EntityConflictException
     */
    Clan createClan(int clanId, ClanRequest clanRequest) throws EntityConflictException;

    /**
     * Adds new random character to the provided clan.
     *
     * @param clan the clan
     * @return the created character
     */
    Character hireCharacter(Clan clan);
}
