package com.withergate.api.repository.item;

import com.withergate.api.model.item.Outfit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Outfit repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Integer> {
}
