package com.withergate.api.game.repository.faction;

import com.withergate.api.game.model.faction.Faction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Faction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface FactionRepository extends JpaRepository<Faction, String> {

}
