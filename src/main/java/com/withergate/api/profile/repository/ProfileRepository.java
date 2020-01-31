package com.withergate.api.profile.repository;

import com.withergate.api.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Profile repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    Profile findOneByName(String name);

}
