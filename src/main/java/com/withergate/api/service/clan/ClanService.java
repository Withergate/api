package com.withergate.api.service.clan;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.service.exception.EntityConflictException;

import java.util.List;
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
     * @return the created clan
     * @throws EntityConflictException entity conflict
     */
    Clan createClan(int clanId, ClanRequest clanRequest) throws EntityConflictException;

    /**
     * Adds new random character to the provided clan.
     *
     * @param clan the clan
     * @param characterType character type
     * @return the created character
     */
    Character hireCharacter(Clan clan, TavernAction.Type characterType);

    /**
     * Un-assigns all arena characters from all clans.
     */
    void clearArenaCharacters();

    /**
     * Increase clan's information level.
     *
     * @param clan             the clan
     * @param notification     the clan's notification to be updated
     * @param informationLevel the new information level
     */
    void increaseInformationLevel(Clan clan, ClanNotification notification, int informationLevel);

    /**
     * Performs all clan turn updates.
     *
     * @param turnId the turn ID
     */
    void performClanTurnUpdates(int turnId);
}
