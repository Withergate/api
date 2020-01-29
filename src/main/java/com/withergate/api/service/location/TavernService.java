package com.withergate.api.service.location;

import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.request.TavernRequest;
import com.withergate.api.service.exception.InvalidActionException;

/**
 * Tavern service interface.
 *
 * @author Martin Myslik
 */
public interface TavernService {

    /**
     * Loads single tavern offer.
     *
     * @param offerId the offer ID
     * @return the loaded offer
     */
    TavernOffer loadTavernOffer(int offerId);

    /**
     * Loads tavern offers for given clan.
     *
     * @param state the state of the offers
     * @param clan  the clan
     * @return the loaded list of offers
     */
    List<TavernOffer> loadTavernOffers(TavernOffer.State state, Clan clan);

    /**
     * Validates and saves the provided action.
     *
     * @param request the action to be saved
     * @param clanId clan ID
     */
    void saveTavernAction(TavernRequest request, int clanId) throws InvalidActionException;

    /**
     * Handles all pending tavern actions.
     *
     * @param turnId turn ID
     */
    void processTavernActions(int turnId);

    /**
     * Prepare new tavern offers for given clan.
     *
     * @param clan the clan
     */
    void prepareTavernOffers(Clan clan);

    /**
     * Manually refreshes tavern offers for provided clan. Throws an exception if action could not be performed.
     *
     * @param clanId clan ID
     */
    void refreshTavernOffers(int clanId) throws InvalidActionException;

}
