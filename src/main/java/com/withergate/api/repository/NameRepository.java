package com.withergate.api.repository;

import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Name repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface NameRepository extends JpaRepository<Name, String> {

    List<Name> findAllByGender(Gender gender);

}
