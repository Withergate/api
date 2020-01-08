package com.withergate.api.repository.trade;

import java.util.List;

import com.withergate.api.model.trade.MarketOffer;
import com.withergate.api.model.trade.MarketOffer.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Market offer repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface MarketOfferRepository extends JpaRepository<MarketOffer, Integer> {

    Page<MarketOffer> findAllByStateOrderByPriceAsc(State state, Pageable pageable);
    List<MarketOffer> findAllByState(State state);

}
