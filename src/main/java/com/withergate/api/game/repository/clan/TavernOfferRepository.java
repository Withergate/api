package com.withergate.api.game.repository.clan;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.TavernOffer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Tavern offer repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface TavernOfferRepository extends JpaRepository<TavernOffer, Integer> {

    List<TavernOffer> findAllByStateAndClan(TavernOffer.State state, Clan clan);
    List<TavernOffer> findAllByClan(Clan clan);

}
