package com.withergate.api.service.clan;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.TavernOffer;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.model.request.DefaultActionRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ClanService interface.
 *
 * @author Martin Myslik
 */
public interface ClanService {

    /**
     * Retrieves a clan by its ID.
     *
     * @param clanId the clan ID
     * @return the clan or null if not found
     */
    Clan getClan(int clanId);

    /**
     * Retrieves all clans.
     *
     * @return all clans in a list
     */
    List<Clan> getAllClans();

    /**
     * Retrieves the list of clans. Supports paging and sorting.
     *
     * @param pageable pagination and sorting
     * @return the page containing clans in the specified order
     */
    Page<Clan> getClans(Pageable pageable);

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
     * @param turn        turn in which the clan is created
     * @return the created clan
     * @throws EntityConflictException entity conflict
     * @throws ValidationException     validation error
     */
    Clan createClan(int clanId, ClanRequest clanRequest, int turn) throws EntityConflictException, ValidationException;

    /**
     * Adds new random character to the provided clan.
     *
     * @param clan the clan
     * @return the created character
     */
    Character hireCharacter(Clan clan);

    /**
     * Performs all clan turn updates.
     *
     * @param turnId the turn ID
     */
    void performClanTurnUpdates(int turnId);

    /**
     * Changes clan's default action.
     *
     * @param request the request
     * @param clanId  the clan ID
     */
    void changeDefaultAction(DefaultActionRequest request, int clanId);

    /**
     * Loads all tavern offers for given clan.
     *
     * @param clanId the clan ID
     * @return list of available clan offers
     */
    List<TavernOffer> loadTavernOffers(int clanId);

}
