package com.withergate.api.repository.disaster;

import java.util.List;

import com.withergate.api.model.disaster.DisasterDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DisasterDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface DisasterDetailsRepository extends JpaRepository<DisasterDetails, String> {

    List<DisasterDetails> findAllByFinalDisaster(boolean finalDisaster);

}
