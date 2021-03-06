package com.withergate.api.service.location;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.TavernOffer;
import com.withergate.api.game.model.request.TavernRequest;
import com.withergate.api.service.action.Actionable;
import com.withergate.api.service.exception.InvalidActionException;

import java.util.List;

/**
 * Tavern service interface.
 *
 * @author Martin Myslik
 */
public interface TavernService extends Actionable<TavernRequest> {

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
