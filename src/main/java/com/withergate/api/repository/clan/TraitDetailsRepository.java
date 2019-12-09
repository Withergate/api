package com.withergate.api.repository.clan;

import com.withergate.api.model.character.TraitDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TraitDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface TraitDetailsRepository extends JpaRepository<TraitDetails, String> {

}
