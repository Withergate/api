package com.withergate.api.service.clan;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.dto.ClanIntelDTO;
import com.withergate.api.game.model.request.ClanRequest;
import com.withergate.api.game.model.request.DefaultActionRequest;
import com.withergate.api.service.exception.EntityConflictException;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
     * Retrieves all clans sorted by fame.
     *
     * @return all clans in a list
     */
    List<Clan> getAllClansByFame();

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

    /**
     * Prepares and saves statistics for every clan.
     *
     * @param turnId turn ID
     */
    void prepareStatistics(int turnId);

    /**
     * Reveals clan intel about the target based on spy's information level.
     *
     * @param targetId target clan ID
     * @param spyId spy clan ID
     * @return clan intel
     * @throws InvalidActionException if clans don't exist
     */
    ClanIntelDTO getClanIntel(int targetId, int spyId) throws InvalidActionException;

    /**
     * Renames the clan's defender. Premium feature.
     *
     * @param clanId clan ID
     * @param name new name
     * @throws InvalidActionException if name is invalid
     */
    void renameDefender(int clanId, String name) throws InvalidActionException;

    /**
     * Handles loan request.
     *
     * @param clanId clanId
     * @throws InvalidActionException
     */
    void processLoan(int clanId) throws InvalidActionException;

}
