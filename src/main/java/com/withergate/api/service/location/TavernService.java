package com.withergate.api.service.location;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.character.CharacterFilter;
import com.withergate.api.model.character.TavernOffer;

import java.util.List;

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
     * Saves the provided action.
     *
     * @param action the action to be saved
     */
    void saveTavernAction(TavernAction action);

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
     * @param filter character filter
     */
    void prepareTavernOffers(Clan clan, CharacterFilter filter);

}
